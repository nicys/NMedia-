package ru.netology.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()
    val like = repository.like()
    val shara = repository.share()
}