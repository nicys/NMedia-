package ru.netology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.databinding.FragmentPhotoImageBinding
import ru.netology.dto.Post
import ru.netology.util.PostArg
import ru.netology.util.StringArg
import ru.netology.view.load
import ru.netology.viewmodel.PostViewModel

class PhotoImageFragment : Fragment() {

    companion object {
        var Bundle.postPhoto: String? by StringArg
        var Bundle.postData: Post? by PostArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPhotoImageBinding.inflate(inflater, container, false)

        with(viewModel) {
            arguments?.postData?.let {
                getPostById(it.id).observe(viewLifecycleOwner, { post ->
                    post ?: return@observe
                })
            }
            arguments?.postPhoto?.let {
                binding.fullScreenPhoto.load("${BuildConfig.BASE_URL}/media/")
            }
            return binding.root
        }
    }
}