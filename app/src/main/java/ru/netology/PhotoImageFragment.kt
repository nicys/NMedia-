package ru.netology

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.netology.databinding.FragmentShowPostBinding
import ru.netology.dto.Post
import ru.netology.util.PostArg
import ru.netology.viewmodel.PostViewModel

class PhotoImageFragment : Fragment() {

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
    ): View? {
        val binding = FragmentShowPostBinding.inflate(inflater, container, false)

        with(viewModel) {
            arguments?.postData?.let {
                val showPhotoImage = it

                getPostById(it.id).observe(viewLifecycleOwner, { post ->
                    post ?: return@observe

                    binding.apply {
//                            author.text = showPhotoImage.author

                        showPhotoImage.attachment
//                            .load("${BuildConfig.BASE_URL}/media/") = showPhotoImage.attachment
                    }
                })
            }
        }
        return binding.root
    }
}