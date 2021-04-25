package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Query
import ru.netology.dto.Post

interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id")
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post): Post
}
