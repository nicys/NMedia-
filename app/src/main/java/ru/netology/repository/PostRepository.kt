package ru.netology.repository

import ru.netology.dto.Post

interface PostRepository {

    fun getAllAsync(callback: Callback<List<Post>>)
    fun likeByIdAsyn(callback: Callback<Post>, id: Long)
    fun dislikeByIdAsyn(callback: Callback<Post>, id: Long)
    fun shareByIdAsyn(callback: Callback<Post>, id: Long)
    fun removeByIdAsyn(callback: Callback<Unit>, id: Long)
    fun saveAsyn(callback: Callback<Post>, post: Post)

    interface Callback<T> {
        fun onSuccess(value: T) {}
        fun onError(e: Exception) {}
    }
}