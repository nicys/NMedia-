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

    fun likeByIdAsync(callback: GetPostCallback, id: Long)
//    fun unLikeByIdAsync(callback: GetPostCallback, id: Long)
    fun shareByIdAsync(callback: GetPostCallback, id: Long)
    fun removeByIdAsync(callback: GetPostCallback, id: Long)
    fun saveAsync(callback: GetPostCallback, post: Post)

    interface GetPostCallback {
        fun onSuccess(post: Post) {}
        fun onError(e: Exception) {}
    }
}