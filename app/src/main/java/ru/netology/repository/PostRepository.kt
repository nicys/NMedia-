package ru.netology.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.dto.Post

interface PostRepository {

    val data: Flow<List<Post>>
    suspend fun getAll()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun save(post: Post)
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
//    suspend fun unlikeById(id: Long)
    suspend fun shareById(id: Long)
}