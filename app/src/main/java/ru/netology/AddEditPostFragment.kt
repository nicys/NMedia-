package ru.netology

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.databinding.FragmentAddEditPostBinding
import ru.netology.util.AndroidUtils.hideKeyboard
import ru.netology.util.StringArg

// класс обработки входящего интента и возврата результата

class AddEditPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

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
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) {
                val toast = Toast.makeText(context, getString(R.string.error_empty_content), Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.TOP, 0, 300)
                toast.show()
                activity?.setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.edit.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                activity?.setResult(Activity.RESULT_OK, intent)
            }
            findNavController().navigateUp()
        }
        return binding.root
    }
}