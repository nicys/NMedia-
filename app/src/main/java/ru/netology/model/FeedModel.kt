package ru.netology.model

import ru.netology.dto.Post

//Отвечает непосредственно за данные (список постов)
data class FeedModel(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false,
)
