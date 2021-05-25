package ru.netology.repository

import retrofit2.Callback
import retrofit2.http.POST
import ru.netology.dto.Post

interface PostRepository {

//    fun getAll(): List<Post>
//    fun likeById(id: Long)
//    fun shareById(id: Long)
//    fun removeById(id: Long)
//    fun save(post: Post)

    fun getAllAsync(callback: Callback<List<Post>>)

//    interface GetAllCallback {
//        fun onSuccess(posts: List<Post>) {}
//        fun onError(e: Exception) {}
//    }

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