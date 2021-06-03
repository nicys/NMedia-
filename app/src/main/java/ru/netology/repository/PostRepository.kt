package ru.netology.repository

import ru.netology.dto.Post

interface PostRepository {

    suspend fun getAllAsync(): List<Post>
    suspend fun likeByIdAsyn(id: Long)
    suspend fun dislikeByIdAsyn(id: Long)
    suspend fun shareByIdAsyn(id: Long)
    suspend fun removeByIdAsyn(id: Long)
    suspend fun saveAsyn(post: Post)

//    interface Callback<T> {
//        fun onSuccess(value: T) {}
//        fun onError(e: Exception) {}
//    }
}