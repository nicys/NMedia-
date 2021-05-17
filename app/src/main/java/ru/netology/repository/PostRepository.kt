package ru.netology.repository

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

    fun likeByIdAsync(id: Long, callback: GetIdCallback)
    fun shareByIdAsync(id: Long, callback: GetIdCallback)
    fun removeByIdAsync(id: Long, callback: GetIdCallback)

    interface GetIdCallback {
        fun onSuccess(id: Long) {}
        fun onError(e: Exception) {}
    }

    fun saveAsync(post: Post, callback: GetPostCallback)

    interface GetPostCallback {
        fun onSuccess(post: Post) {}
        fun onError(e: Exception) {}
    }
}