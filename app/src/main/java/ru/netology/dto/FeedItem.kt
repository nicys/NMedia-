package ru.netology.dto

import kotlinx.parcelize.RawValue
import ru.netology.enumeration.AttachmentType

sealed class FeedItem{
    abstract val id: Long
}

data class Ad(
    override val id: Long,
    val url: String,
    val image: String,
) : FeedItem()

data class Post(
    override val id: Long,
    val author: String,
    val authorId: Long,
    val authorAvatar: String,
    val published: Long,
    val content: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shares: String? = "0",
    val sharesCnt: Int = 0,
    val video: String? = null,
    val attachment: @RawValue Attachment? = null,
    val ownedByMe: Boolean = false,
) : FeedItem()

data class Attachment(
    val url: String,
    val type: AttachmentType,
)
