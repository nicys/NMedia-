package ru.netology.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import ru.netology.enumeration.AttachmentType

@Parcelize
data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shares: String? = "0",
    val sharesCnt: Int = 0,
    val video: String? = null,
    val attachment: @RawValue Attachment? = null,
) : Parcelable

data class Attachment(
    val url: String,
    val type: AttachmentType,
)