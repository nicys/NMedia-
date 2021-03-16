package ru.netology.repository

import androidx.lifecycle.LiveData
import ru.netology.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun like(id: Long)
    fun share(id: Long)
}