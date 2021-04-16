package ru.netology

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import ru.netology.AddEditPostFragment.Companion.textArg
import ru.netology.ShowPostFragment.Companion.postShow

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

            val post = it.getStringExtra(Intent.EXTRA_TEXT)
            if (post?.isNotBlank() != true) {
                return@let
            }
//            intent.removeExtra(Intent.EXTRA_TEXT)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_nav_host, ShowPostFragment()).commit()
            findNavController(R.id.fragment_nav_host).navigate(
                R.id.action_feedFragment_to_showPostFragment,
                Bundle().apply {
                    postShow = post
                }
            )
        }
    }
}