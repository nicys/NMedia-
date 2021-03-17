package ru.netology

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.adapter.PostsAdapter
import ru.netology.databinding.ActivityMainBinding
import ru.netology.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(
                onLikeListener = { viewModel.likeById(it.id) },
                onShareListener = { viewModel.shareById(it.id) }
        )
        binding.list.adapter = adapter
        viewModel.data.observe(this, { posts ->
            adapter.submitList(posts)
        })
    }
}