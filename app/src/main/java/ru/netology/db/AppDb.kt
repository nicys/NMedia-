package ru.netology.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.netology.dao.PostDao
import ru.netology.dao.PostWorkDao
import ru.netology.entity.PostEntity
import ru.netology.entity.PostWorkEntity

@Database(entities = [PostEntity::class, PostWorkEntity::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postWorkDao(): PostWorkDao
}