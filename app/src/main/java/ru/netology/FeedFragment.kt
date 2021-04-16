package ru.netology

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostsAdapter
import ru.netology.databinding.FragmentFeedBinding
import ru.netology.dto.Post
import ru.netology.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.published)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
                viewModel.shareById(post.id)
            }

            override fun onVideo(post: Post) {
                post.video?.let {
                    val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        Intent(Intent.ACTION_VIEW, Uri.parse("url"))
                        setData(Uri.parse(post.video))
                    }
                    val videoIntent = Intent.createChooser(intent, getString(R.string.chooser_video_post))
                    startActivity(videoIntent)
                }
            }

            override fun onClickPost(post: Post) {
                findNavController().navigate(R.id.action_feedFragment_to_showPostFragment)
//                post.content?.let {
//                    val intent = Intent().apply {
//                        action = Intent.ACTION_SEND
//                        putExtra(Intent.EXTRA_TEXT, post.content)
//                        type = "text/plain"
//                    }
//                }
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, { posts ->
            adapter.submitList(posts)
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_addEditPostFragment)
        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id == 0L) {
                return@observe
            }
            findNavController().navigate(R.id.action_feedFragment_to_addEditPostFragment)
        }

//        binding.list.setOnClickListener {
//            findNavController().navigate(R.id.action_feedFragment_to_showPostFragment)
//
//
//        }

        return binding.root
    }
}