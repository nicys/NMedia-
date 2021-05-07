package ru.netology

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.AddEditPostFragment.Companion.textData
import ru.netology.databinding.FragmentAddEditPostBinding
import ru.netology.databinding.FragmentShowPostBinding
import ru.netology.dto.Post
import ru.netology.util.AndroidUtils
import ru.netology.util.PostArg
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
    ): View? {
        val binding = FragmentAddEditPostBinding.inflate(inflater, container, false)
        binding.edit.requestFocus()

        arguments?.textArg?.let(binding.edit::setText)

        binding.ok.setOnClickListener {
            viewModel.changeContent(binding.edit.text.toString())
            viewModel.save()
            savecontent("")
            AndroidUtils.hideKeyboard(requireView())
        }
        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val content = binding.edit.text.toString()
            savecontent(content)
            findNavController().navigateUp()
        }
        return binding.root
    }
    fun savecontent(string: String) {
        context?.openFileOutput("savecontent.json", Context.MODE_PRIVATE)?.bufferedWriter().use {
            if (it != null) {
                it.write(string)
            }
        }
    }
}