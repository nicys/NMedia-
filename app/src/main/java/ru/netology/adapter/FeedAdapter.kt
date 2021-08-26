package ru.netology.adapter

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.BuildConfig
import ru.netology.R
import ru.netology.databinding.CardAdBinding
import ru.netology.databinding.CardPostBinding
import ru.netology.dto.Ad
import ru.netology.dto.FeedItem
import ru.netology.dto.Post
import ru.netology.view.load
import ru.netology.view.loadCircleCrop

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
    fun onVideo(post: Post) {}
    fun onShowPost(post: Post) {}
    fun onPhotoImage(post: Post) {}
    fun onAdClick(ad: Ad) {}
}

class FeedAdapter(
    private val onInteractionListener: OnInteractionListener,
) : PagingDataAdapter<FeedItem, RecyclerView.ViewHolder>(PostViewHolder.FeedItemDiffCallback()) {
    private val typeAd = 0
    private val typePost = 1

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Ad -> typeAd
            is Post -> typePost
            null -> throw IllegalArgumentException("unknown item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            typeAd -> PostViewHolder.AdViewHolder(
                CardAdBinding.inflate(layoutInflater, parent, false),
                onInteractionListener
            )
            typePost -> PostViewHolder(
                CardPostBinding.inflate(layoutInflater, parent, false),
                onInteractionListener
            )
            else -> throw IllegalArgumentException("unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // FIXME: students will do in HW
        getItem(position)?.let {
            when (it) {
                is Post -> (holder as? PostViewHolder)?.bind(it)
                is Ad -> (holder as? PostViewHolder.AdViewHolder)?.bind(it)
            }
        }
    }

    class PostViewHolder(
        private val binding: CardPostBinding,
        private val onInteractionListener: OnInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.apply {
                author.text = post.author
                published.text = post.published.toString()
                content.text = post.content
                avatar.loadCircleCrop("${BuildConfig.BASE_URL}/avatars/${post.authorAvatar}")

                if (post.attachment != null) {
                    photoImage.visibility = VISIBLE
                    photoImage.load("${BuildConfig.BASE_URL}/media/${post.attachment.url}")
                } else photoImage.visibility = GONE

                if (post.video != null) {
                    video.visibility = VISIBLE
                } else video.visibility = GONE

                share.text = totalizerSmartFeed(post.sharesCnt)
                like.isChecked = post.likedByMe
                like.text = if (post.likedByMe) "1" else "0"

                like.setOnClickListener {
                    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1F, 1.25F, 1F)
                    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1F, 1.25F, 1F)
                    ObjectAnimator.ofPropertyValuesHolder(it, scaleX, scaleY).apply {
                        duration = 500
                        repeatCount = 100
                        interpolator = BounceInterpolator()
                    }.start()
                    onInteractionListener.onLike(post)
                }
                share.setOnClickListener {
                    onInteractionListener.onShare(post)
                }
                video.setOnClickListener {
                    onInteractionListener.onVideo(post)
                }

                content.setOnClickListener {
                    onInteractionListener.onShowPost(post)
                }
                photoImage.setOnClickListener {
                    onInteractionListener.onPhotoImage(post)
                }

                menu.isVisible = post.ownedByMe

                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.option_post)
                        menu.setGroupVisible(R.id.owned, post.ownedByMe)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    onInteractionListener.onRemove(post)
                                    true
                                }
                                R.id.edit -> {
                                    onInteractionListener.onEdit(post)
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }
            }
        }

        class AdViewHolder(
            private val binding: CardAdBinding,
            private val onInteractionListener: OnInteractionListener,
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(ad: Ad) {
                binding.apply {
                    image.load("${BuildConfig.BASE_URL}/media/${ad.image}")
                    image.setOnClickListener {
                        onInteractionListener.onAdClick(ad)
                    }
                }
            }
        }

        class FeedItemDiffCallback : DiffUtil.ItemCallback<FeedItem>() {
            override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
                if (oldItem::class != newItem::class) {
                    return false
                }

                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
                return oldItem == newItem
            }
        }

        class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }

        private fun counterOverThousand(feed: Int): Int {
            return when (feed) {
                in 1_000..999_999 -> feed / 100
                else -> feed / 100_000
            }
        }

        fun totalizerSmartFeed(feed: Int): String {
            return when (feed) {
                in 0..999 -> "$feed"
                in 1_000..999_999 -> "${(counterOverThousand(feed).toDouble() / 10)}K"
                else -> "${(counterOverThousand(feed).toDouble() / 10)}M"
            }
        }
    }
}