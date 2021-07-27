package ru.netology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.databinding.FragmentShowPostBinding
import ru.netology.dto.Post
//import ru.netology.util.PostArg
import ru.netology.viewmodel.PostViewModel

@AndroidEntryPoint
class ShowPostFragment : Fragment() {

//    companion object {
//        var Bundle.postData: Post? by PostArg
//    }
//
//    private val viewModel: PostViewModel by viewModels(
//        ownerProducer = ::requireParentFragment
//    )
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val binding = FragmentShowPostBinding.inflate(inflater, container, false)

//        with(viewModel) {
//            arguments?.postData?.let {
//                val showPost = it

//                    getPostById(it.id).observe(viewLifecycleOwner, { post ->
//                    post ?: return@observe
//
//                    binding.apply {
//                        author.text = showPost.author
//                        published.text = showPost.published
//                        content.text = showPost.content
//                        share.text = totalizerSmartFeed(showPost.sharesCnt)
//                        like.isChecked = showPost.likeByMe
//                        like.text = if (showPost.likeByMe) "1" else "0"
//
//                        menu.setOnClickListener {
//                            PopupMenu(it.context, it).apply {
//                                inflate(R.menu.option_post)
//                                setOnMenuItemClickListener { item ->
//                                    when (item.itemId) {
//                                        R.id.remove -> {
//                                            removeById(showPost.id)
//                                            findNavController().navigate(R.id.action_showPostFragment_to_feedFragment)
//                                            true
//                                        }
//                                        R.id.edit -> {
//                                            findNavController().navigate(R.id.action_showPostFragment_to_addEditPostFragment,
//                                                Bundle().apply
//                                                { textData = showPost.content })
//                                            true
//                                        }
//                                        else -> false
//                                    }
//                                }
//                            }.show()
//                        }
//
//                        like.setOnClickListener {
//                            likeById(showPost.id)
//                        }
//
//                        share.setOnClickListener {
//                            val intent = Intent().apply {
//                                action = Intent.ACTION_SEND
//                                putExtra(Intent.EXTRA_TEXT, showPost.published)
//                                type = "text/plain"
//                            }
//                            val shareIntent =
//                                Intent.createChooser(intent, getString(R.string.chooser_share_post))
//                            startActivity(shareIntent)
//                            shareById(showPost.id)
//                        }
//
//                        video.setOnClickListener {
//                            showPost.video?.let {
//                                val intent = Intent().apply {
//                                    action = Intent.ACTION_VIEW
//                                    Intent(Intent.ACTION_VIEW, Uri.parse("url"))
//                                    data = Uri.parse(showPost.video)
//                                }
//                                val videoIntent =
//                                    Intent.createChooser(
//                                        intent,
//                                        getString(R.string.chooser_video_post)
//                                    )
//                                startActivity(videoIntent)
//                            }
//                        }
//                    }
//                })
//            }
//        }
//        return binding.root
//    }
}


//arguments?.postData?.let {
//    val showPost = it
//    binding.apply {
//        author.text = showPost.author
//        published.text = showPost.published
//        content.text = showPost.content
//        share.text = viewModel.totalizerSmartFeed(showPost.sharesCnt)
//        like.isChecked = showPost.likeByMe
//        like.text = if (showPost.likeByMe) "1" else "0"
//        menu.setOnClickListener {
//            PopupMenu(it.context, it).apply {
//                inflate(R.menu.option_post)
//                setOnMenuItemClickListener { item ->
//                    when (item.itemId) {
//                        R.id.remove -> {
//                            viewModel.removeById(showPost.id)
//                            findNavController().navigate(R.id.action_showPostFragment_to_feedFragment)
//                            true
//                        }
//                        R.id.edit -> {
//                            findNavController().navigate(R.id.action_showPostFragment_to_addEditPostFragment,
//                                Bundle().apply
//                                { textData = showPost.content })
//                            true
//                        }
//                        else -> false
//                    }
//                }
//            }.show()
//        }
//        like.setOnClickListener {
//            viewModel.likeById(showPost.id)
//        }
//        share.setOnClickListener {
//            val intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                putExtra(Intent.EXTRA_TEXT, showPost.published)
//                type = "text/plain"
//            }
//            val shareIntent =
//                Intent.createChooser(intent, getString(R.string.chooser_share_post))
//            startActivity(shareIntent)
//            viewModel.shareById(showPost.id)
//        }
//        video.setOnClickListener {
//            showPost.video?.let {
//                val intent = Intent().apply {
//                    action = Intent.ACTION_VIEW
//                    Intent(Intent.ACTION_VIEW, Uri.parse("url"))
//                    data = Uri.parse(showPost.video)
//                }
//                val videoIntent =
//                    Intent.createChooser(intent, getString(R.string.chooser_video_post))
//                startActivity(videoIntent)
//            }
//        }
//    }
//}
