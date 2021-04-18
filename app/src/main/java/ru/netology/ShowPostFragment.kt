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
import ru.netology.repository.totalizerSmartFeed
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

        arguments?.postData?.let {
            val showPost = it
            with(binding) {
                author.text = showPost.author
                published.text = showPost.published
                content.text = showPost.content
                share.text = totalizerSmartFeed(showPost.sharesCnt)
                like.isChecked = showPost.likeByMe
                like.text = if (showPost.likeByMe) "1" else "0"

            }
        }
        return binding.root
    }
}