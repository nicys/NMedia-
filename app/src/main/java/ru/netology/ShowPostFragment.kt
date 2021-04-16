package ru.netology

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.AddEditPostFragment.Companion.textArg
import ru.netology.databinding.FragmentShowPostBinding
import ru.netology.util.AndroidUtils
import ru.netology.util.StringArg
import ru.netology.viewmodel.PostViewModel

class ShowPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
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

        binding.content.requestFocus()

        arguments?.textArg?.let {
            binding.content.setText(it)
        }

        val intent = Intent()
        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }
            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            with(binding.content) {
                setText(text)
                requestFocus()
            }
        }

        return binding.root
    }
}