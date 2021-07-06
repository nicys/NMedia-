package ru.netology.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.dto.Post

@Entity
data class PostWorkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val postId: Long,
    val author: String,
    val authorId: Long,
    val authorAvatar: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shares: String?,
    val sharesCnt: Int,
    val video: String? = null,
    @Embedded
    var attachment: AttachmentEmbeddable?,
    var uri: String? = null,
) {
    fun toDto() = Post(postId, author,
        authorId, authorAvatar, published, content, likedByMe, likes, shares, sharesCnt, video, attachment?.toDto(),
    )

    companion object {
        fun fromDto(dto: Post) =
            PostWorkEntity(
                0L,
                dto.id,
                dto.author,
                dto.authorId,
                dto.authorAvatar,
                dto.published,
                dto.content,
                dto.likedByMe,
                dto.likes,
                dto.shares,
                dto.sharesCnt,
                dto.video,
                AttachmentEmbeddable.fromDto(dto.attachment)
            )
    }
}