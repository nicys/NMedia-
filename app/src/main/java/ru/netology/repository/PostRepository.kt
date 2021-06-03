package ru.netology.repository

import ru.netology.dto.Post

interface PostRepository {

    suspend fun getAllAsync(callback: Callback<List<Post>>)
    suspend fun likeByIdAsyn(callback: Callback<Post>, id: Long)
    suspend fun dislikeByIdAsyn(callback: Callback<Post>, id: Long)
    suspend fun shareByIdAsyn(callback: Callback<Post>, id: Long)
    suspend fun removeByIdAsyn(callback: Callback<Unit>, id: Long)
    suspend fun saveAsyn(callback: Callback<Post>, post: Post)

    interface Callback<T> {
        fun onSuccess(value: T) {}
        fun onError(e: Exception) {}
    }
}