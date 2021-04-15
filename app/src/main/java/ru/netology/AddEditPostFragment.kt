package ru.netology

import android.content.Intent
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
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
            ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentAddEditPostBinding.inflate(inflater, container, false)

        //      обработка меню edit
        binding.edit.requestFocus()

        arguments?.textArg?.let {
            binding.edit.setText(it)
        }

        val intent = Intent()
        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }
            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            with(binding.edit) {
                setText(text)
                requestFocus()
                hideKeyboard(this)
            }
        }
        //      обработка button ok(save)
        binding.ok.setOnClickListener {
            viewModel.changeContent(binding.edit.text.toString())
            viewModel.save()
            hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        return binding.root
    }
}