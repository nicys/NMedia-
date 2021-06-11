package ru.netology.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.BuildConfig
import ru.netology.R
import ru.netology.databinding.CardPostBinding
import ru.netology.dto.Post
import ru.netology.enumeration.AttachmentType
import ru.netology.view.load
import ru.netology.view.loadCircleCrop
//import ru.netology.view.loadPhoto

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
    fun onVideo(post: Post) {}
    fun onShowPost(post: Post) {}
    fun onShowPhoto(post: Post) {}
}

class PostsAdapter(
        private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
        private val binding: CardPostBinding,
        private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            avatar.loadCircleCrop("${BuildConfig.BASE_URL}/avatars/${post.authorAvatar}")

            if (post.attachment != null) {
                photo.visibility = View.VISIBLE
                photo.load("${BuildConfig.BASE_URL}/media/${post.attachment?.url}")
            } else photo.visibility = View.GONE

            if (post.video != null) {
                video.visibility = View.VISIBLE
            } else video.visibility = View.GONE

            share.text = totalizerSmartFeed(post.sharesCnt)
            like.isChecked = post.likedByMe
            like.text = if (post.likedByMe) "1" else "0"

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            video.setOnClickListener {
                onInteractionListener.onVideo(post)
            }

            postCard.setOnClickListener {
                onInteractionListener.onShowPost(post)
            }
            postCard.setOnClickListener {
                onInteractionListener.onShowPhoto(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.option_post)
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