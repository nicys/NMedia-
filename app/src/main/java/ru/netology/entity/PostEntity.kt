package ru.netology.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val published: String,
    val content: String,
    val likeByMe: Boolean,
    val like: Int = 0,
    val shares: String?,
    val sharesCnt: Int,
    val video: String? = null
) {
    fun toDto() = Post(id, author, authorAvatar, content, published, likeByMe, like, shares, sharesCnt, video)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.published,
                dto.likedByMe,
                dto.likes,
                dto.shares,
                dto.sharesCnt,
                dto.video)

    }
}