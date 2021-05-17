package ru.netology.repository

import androidx.lifecycle.LiveData
import ru.netology.dto.Post

interface PostRepository {
//    fun getAll(): List<Post>
//    fun likeById(id: Long)
//    fun shareById(id: Long)
//    fun removeById(id: Long)
//    fun save(post: Post)

    fun getAllAsync(callback: GetAllCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>) {}
        fun onError(e: Exception) {}
    }

    fun likeById(id: Long, callback: GetIdCallback)
    fun shareById(id: Long, callback: GetIdCallback)
    fun removeById(id: Long, callback: GetIdCallback)

    interface GetIdCallback {
        fun onSuccess(id: Long) {}
        fun onError(e: Exception) {}
    }

    fun save(post: Post, callback: GetPostCallback)

    interface GetPostCallback {
        fun onSuccess(post: Post) {}
        fun onError(e: Exception) {}
    }
}