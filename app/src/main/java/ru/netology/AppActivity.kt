package ru.netology

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import ru.netology.AddEditPostFragment.Companion.textArg

class AppActivity : AppCompatActivity(R.layout.activity_app) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) {
                return@let
            }
            intent.removeExtra(Intent.EXTRA_TEXT)
            findNavController(R.id.fragment_nav_host).navigate(
                    R.id.action_feedFragment_to_addEditPostFragment,
                    Bundle().apply {
                        textArg = text
                    }
            )
        }
    }
}

//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import ru.netology.databinding.ActivityAppBinding
//
//class AppActivity : AppCompatActivity(R.layout.activity_app) {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val binding = ActivityAppBinding.inflate(layoutInflater)
//
//        intent?.let {
//            if (it.action != Intent.ACTION_SEND) {
//                return@let
//            }
//
//            val text = it.getStringExtra(Intent.EXTRA_TEXT)
//            if (text.isNullOrBlank()) {
//                val toast = Toast.makeText(applicationContext, getString(R.string.error_empty_content), Toast.LENGTH_LONG)
//                toast.show()
//                return@let
//            } else {
//                with(binding.textOfPost) {
//                    setText(text)
//                }
//            }
//        }
//    }
//}