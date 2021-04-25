package ru.netology.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likeByMe: Boolean,
    val like: Int = 0,
    val shares: String,
    val sharesCnt: Int,
    val video: String?
)