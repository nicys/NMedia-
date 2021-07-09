package ru.netology.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.dto.Media
import ru.netology.dto.MediaUpload
import ru.netology.dto.Post

interface PostRepository {

    val data: Flow<List<Post>>
    suspend fun getAll()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
//    suspend fun unlikeById(id: Long)
    suspend fun shareById(id: Long)
    suspend fun upload(upload: MediaUpload): Media
    suspend fun authentication(login: String, password: String)
    suspend fun registration(nameUser: String, login: String, password: String)
    suspend fun saveWork(post: Post, upload: MediaUpload?): Long
    suspend fun processWork(id: Long)
    suspend fun processWorkRemoved(id: Long)
}

//suspend fun save(post: Post)
//suspend fun saveWithAttachment(post: Post, upload: MediaUpload)