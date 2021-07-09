package ru.netology.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.netology.dao.Converters
import ru.netology.dao.PostDao
import ru.netology.dao.PostWorkDao
import ru.netology.entity.PostEntity
import ru.netology.entity.PostWorkEntity

@Database(entities = [PostEntity::class, PostWorkEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postWorkDao(): PostWorkDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
//                .allowMainThreadQueries()
/*Отключили, т.к. нельзя исльпользовать ROOM в UI потоке. По сути .allowMainThreadQueries()  нужна только для тестирования.
При корутинах не используется - все запросы уходят из основного потока.*/
                .fallbackToDestructiveMigration()
                .build()
    }
}