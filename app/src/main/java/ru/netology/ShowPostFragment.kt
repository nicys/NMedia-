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
import ru.netology.AddEditPostFragment.Companion.textArg
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostsAdapter
import ru.netology.databinding.FragmentShowPostBinding
import ru.netology.dto.Post
import ru.netology.util.StringArg
import ru.netology.viewmodel.PostViewModel

class ShowPostFragment : Fragment() {

    companion object {
        var Bundle.postShow: String? by StringArg
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

//        arguments?.postShow?.let {
//            binding.postList.setText(it)
//        }

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
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
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
                    val videoIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_video_post))
                    startActivity(videoIntent)
                }
            }

            override fun onClickPost(post: Post) {
                findNavController().navigate(R.id.action_feedFragment_to_showPostFragment)
            }
        })

//        binding.postListScroll.adapter = adapter
//        viewModel.data.observe(viewLifecycleOwner, { post ->
//            adapter.submitList(post)
//        })

//        viewModel.edited.observe(viewLifecycleOwner) { post ->
//            if (post.id == 0L) {
//                return@observe
//            }
//        }


        return binding.root
    }
}