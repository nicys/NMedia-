package ru.netology.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.netology.entity.PostEntity
import ru.netology.enumeration.AttachmentType

@Dao
interface PostDao {
/* Не указываем модификатор suspend, т.к. возвращаем LiveData */
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): Flow<List<PostEntity>>

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Long)

    @Query(
        """
        UPDATE PostEntity SET
        `likes` = `likes` + CASE WHEN likeByMe THEN -1 ELSE 1 END,
        likeByMe = CASE WHEN likeByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    suspend fun likeById(id: Long)

    @Query(
        """
        UPDATE PostEntity SET
        sharesCnt = sharesCnt + 1
        WHERE id = :id
        """
    )
    suspend fun shareById(id: Long)
}

class Converters {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)
    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name
}
