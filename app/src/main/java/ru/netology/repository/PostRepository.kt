package ru.netology.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.dto.Post
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload

interface PostRepository {

    val data: Flow<List<Post>>
    suspend fun getAll()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload)
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
//    suspend fun unlikeById(id: Long)
    suspend fun shareById(id: Long)
    suspend fun upload(upload: MediaUpload): Media
    suspend fun authentication(login: String, password: String)
}