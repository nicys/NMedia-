package ru.netology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.databinding.FragmentAddEditPostBinding
import ru.netology.util.AndroidUtils
import ru.netology.util.AndroidUtils.hideKeyboard
import ru.netology.util.StringArg
import ru.netology.viewmodel.PostViewModel

// класс обработки входящего интента и возврата результата

class AddEditPostFragment : Fragment() {

    companion object {
        var Bundle.textData: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
            ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentAddEditPostBinding.inflate(inflater, container, false)

        arguments?.textData?.let {
            binding.edit.setText(it)
        }

        binding.ok.setOnClickListener {
            viewModel.changeContent(binding.edit.text.toString())
            viewModel.save()
            hideKeyboard(requireView())
        }

        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        return binding.root
    }
}