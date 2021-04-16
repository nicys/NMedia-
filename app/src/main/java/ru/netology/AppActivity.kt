package ru.netology

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
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

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) {
                return@let
            }
            intent.removeExtra(Intent.EXTRA_TEXT)
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_nav_host, ShowPostFragment()).commit()
            findNavController(R.id.fragment_nav_host).navigate(
                    R.id.action_feedFragment_to_showPostFragment,
                    Bundle().apply {
                        textArg = text
                    }
            )
        }
    }

    private fun onShowPost() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_nav_host, ShowPostFragment()).commit()
    }
}
