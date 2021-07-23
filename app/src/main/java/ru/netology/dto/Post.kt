package ru.netology.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import ru.netology.enumeration.AttachmentType

@Parcelize
data class Post(
    val id: Long,
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
) : Parcelable

data class Attachment(
    val url: String,
    val type: AttachmentType,
)