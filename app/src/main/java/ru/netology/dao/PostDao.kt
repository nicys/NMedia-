package ru.netology.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.entity.PostEntity

@Dao
interface PostDao {
/* Не указываем модификатор suspend, т.к. возвращаем LiveData */
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

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

//interface PostDao {
//    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
//    fun getAll(): LiveData<List<PostEntity>>
//
//    @Query(
//        """
//        UPDATE PostEntity SET
//        `likes` = `likes` + CASE WHEN likeByMe THEN -1 ELSE 1 END,
//        likeByMe = CASE WHEN likeByMe THEN 0 ELSE 1 END
//        WHERE id = :id
//        """
//    )
//    suspend fun likeById(id: Long)
//
//    @Query(
//        """
//        UPDATE PostEntity SET
//        sharesCnt = sharesCnt + 1
//        WHERE id = :id
//        """
//    )
//    suspend fun shareById(id: Long)
//
//    @Query("DELETE FROM PostEntity WHERE id = :id")
//    suspend fun removeById(id: Long)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun save(post: PostEntity): Long
//}
