package ru.netology

//import ru.netology.ShowPostFragment.Companion.postData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.NewPostFragment.Companion.textData
import ru.netology.PhotoImageFragment.Companion.postPhoto
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
                findNavController().navigate(R.id.action_feedFragment_to_addEditPostFragment,
                    Bundle().apply { textData = post.content }
                )
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
                    putExtra(Intent.EXTRA_TEXT, post.content)
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

            override fun onPhotoImage(post: Post) {
                findNavController().navigate(R.id.action_feedFragment_to_photoImageFragment,
                    Bundle().apply
                    {
                        postPhoto = post
                    })
            }

//            override fun onShowPost(post: Post) {
//                findNavController().navigate(R.id.action_feedFragment_to_showPostFragment,
//                    Bundle().apply { postData = post }
//                )
//            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, { state ->
            adapter.submitList(state.posts)
            { binding.list.onScrollStateChanged(state.posts.size) } // перемещение вниз (ожидается)
//            smoothScrollToPosition(state.posts.size)
            binding.emptyText.isVisible = state.empty
        })

        binding.list.adapter = adapter
        viewModel.dataState.observe(viewLifecycleOwner, { state ->
            binding.progress.isVisible = state.loading
            binding.swiperefresh.isRefreshing = state.refreshing
            binding.errorGroup.isVisible = state.error
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_loading) { viewModel.loadPosts() }
                    .show()
            }
        })

        viewModel.networkError.observe(viewLifecycleOwner, {
            Snackbar.make(requireView(), getString(R.string.error_network), Snackbar.LENGTH_LONG)
                .show()
        })

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.refreshPosts()
        }

        viewModel.newerCount.observe(viewLifecycleOwner) { state ->
            // TODO: just log it, interaction must be in homework
            println(state)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_addEditPostFragment)
        }







        return binding.root
    }
}





//        viewModel.dataState.observe(viewLifecycleOwner, { state ->
//            adapter.submitList(state.posts) {
//                binding.list.smoothScrollToPosition(state.posts.size)
//            }
//            binding.progress.isVisible = state.loading
//            binding.errorGroup.isVisible = state.error
//        })