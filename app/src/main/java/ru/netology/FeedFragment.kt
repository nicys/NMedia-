package ru.netology

//import ru.netology.ShowPostFragment.Companion.postData

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import ru.netology.NewPostFragment.Companion.textData
import ru.netology.PhotoImageFragment.Companion.postData
import ru.netology.PhotoImageFragment.Companion.postPhoto
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.FeedAdapter
import ru.netology.databinding.FragmentFeedBinding
import ru.netology.dto.Post
import ru.netology.viewmodel.AuthViewModel
import ru.netology.viewmodel.PostViewModel


@AndroidEntryPoint
class FeedFragment : Fragment() {

    @ExperimentalCoroutinesApi
    val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    @ExperimentalCoroutinesApi
    val authViewModel: AuthViewModel by viewModels(ownerProducer = ::requireParentFragment)

    @ExperimentalCoroutinesApi
    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val adapter = FeedAdapter(object : OnInteractionListener {
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
                        postData = post
                        postPhoto = post.attachment?.url
                    })
            }

//            override fun onShowPost(post: Post) {
//                findNavController().navigate(R.id.action_feedFragment_to_showPostFragment,
//                    Bundle().apply { postData = post }
//                )
//            }
        })

        binding.list.adapter = adapter

        lifecycleScope.launchWhenCreated {
            viewModel.dataPaging.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            authViewModel.data.observe(viewLifecycleOwner, {
                adapter.refresh()
            })
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { state ->
                binding.swiperefresh.isRefreshing =
                    state.refresh is LoadState.Loading ||
//                            state.prepend is LoadState.Loading ||
                            state.append is LoadState.Loading
            }
        }

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
            adapter.refresh()
        }

        val badge = requireContext().let { BadgeDrawable.create(it) }
            .apply {
                isVisible = true
                backgroundColor = resources.getColor(R.color.purple_700)
            }
// Устанавливаем значок, когда размеры вьюшки уже известны
        binding.upTab.doOnPreDraw {
            BadgeUtils.attachBadgeDrawable(badge, binding.upTab)
        }
// По умолчанию видимость View.INVISIBLE, а не View.GONE чтобы размер всегда был (Можно через xml задать)
//        binding.upTab.isInvisible = true
        viewModel.newerCount.observe(viewLifecycleOwner) { count ->
            if (count > 0) {
                binding.upTab.isVisible = true
                badge.number = count
            } else {
                binding.upTab.isInvisible = true
            }
        }

        binding.upTab.setOnClickListener {
            binding.list.smoothScrollToPosition(0)
            binding.upTab.visibility = View.GONE
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_addEditPostFragment)
        }
        return binding.root
    }
}