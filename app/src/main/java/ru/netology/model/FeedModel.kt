package ru.netology.model

import ru.netology.dto.Post

data class FeedModel(
    val postS: List<Post> = emptyList(),
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = false,
    val refreshing: Boolean = false,
)
