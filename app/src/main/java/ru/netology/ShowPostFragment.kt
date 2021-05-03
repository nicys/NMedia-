package ru.netology

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.AddEditPostFragment.Companion.textData
import ru.netology.databinding.FragmentShowPostBinding
import ru.netology.dto.Post
import ru.netology.util.PostArg
import ru.netology.viewmodel.PostViewModel

class ShowPostFragment : Fragment() {

    companion object {
        var Bundle.postData: Post? by PostArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentShowPostBinding.inflate(inflater, container, false)

        with(viewModel) {
            arguments?.postData?.let {
                val showPost = it

                getPostById(it.id).observe(viewLifecycleOwner, { post ->
                    post ?: return@observe

                    binding.apply {
                        author.text = post.text
                        published.text = post.text
                        content.text = post.text
                        share.text = totalizerSmartFeed(post.sharesCnt)
                        like.isChecked = like.isChecked
                        like.text = if (post.likeByMe) "1" else "0"

                        menu.setOnClickListener {
                            PopupMenu(it.context, it).apply {
                                inflate(R.menu.option_post)
                                setOnMenuItemClickListener { item ->
                                    when (item.itemId) {
                                        R.id.remove -> {
                                            removeById(post.id)
                                            findNavController().navigate(R.id.action_showPostFragment_to_feedFragment)
                                            true
                                        }
                                        R.id.edit -> {
                                            findNavController().navigate(R.id.action_showPostFragment_to_addEditPostFragment,
                                                Bundle().apply
                                                { textData = post.content })
                                            true
                                        }
                                        else -> false
                                    }
                                }
                            }.show()
                        }

                        like.setOnClickListener {
                            likeById(showPost.id)
                        }

                        share.setOnClickListener {
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, showPost.published)
                                type = "text/plain"
                            }
                            val shareIntent =
                                Intent.createChooser(intent, getString(R.string.chooser_share_post))
                            startActivity(shareIntent)
                            shareById(showPost.id)
                        }

                        video.setOnClickListener {
                            post.video?.let {
                                val intent = Intent().apply {
                                    action = Intent.ACTION_VIEW
                                    Intent(Intent.ACTION_VIEW, Uri.parse("url"))
                                    data = Uri.parse(showPost.video)
                                }
                                val videoIntent =
                                    Intent.createChooser(
                                        intent,
                                        getString(R.string.chooser_video_post)
                                    )
                                startActivity(videoIntent)
                            }
                        }
                    }
                })
            }

        }
        return binding.root
    }
}