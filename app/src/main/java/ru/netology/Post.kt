package ru.netology

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    var content: String,
    var likeByMe: Boolean
)