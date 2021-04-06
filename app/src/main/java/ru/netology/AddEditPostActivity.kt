package ru.netology

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.databinding.ActivityAddEditPostBinding
import ru.netology.util.AndroidUtils.hideKeyboard

// класс обработки входящего интента и возврата результата

class AddEditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //      обработка меню edit
//        binding.edit.setSelectAllOnFocus(true)
        binding.edit.requestFocus()

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
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.edit.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }
}