package ru.netology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.databinding.FragmentPhotoImageBinding
import ru.netology.dto.Post
import ru.netology.util.PostArg
import ru.netology.util.StringArg
import ru.netology.view.load
import ru.netology.viewmodel.PostViewModel

@AndroidEntryPoint
class PhotoImageFragment : Fragment() {

    companion object {
        var Bundle.postPhoto: String? by StringArg
        var Bundle.postData: Post? by PostArg
    }

    @ExperimentalCoroutinesApi
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPhotoImageBinding.inflate(inflater, container, false)

        with(viewModel) {
            arguments?.postData?.let {
                val postWithPhoto = it
                getPostById(postWithPhoto.id).observe(viewLifecycleOwner, { post ->
                    post ?: return@observe
                })
                arguments?.postPhoto?.let {
                    binding.fullScreenPhoto.load("${BuildConfig.BASE_URL}/media/${postWithPhoto.attachment?.url}")
                }
            }
            return binding.root
        }
    }
}
