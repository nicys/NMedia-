package ru.netology.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.netology.repository.PostRepository

@HiltWorker
class SavePostWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: PostRepository,
) : CoroutineWorker(applicationContext, params) {
    companion object {
        const val postKey = "post"
    }

    override suspend fun doWork(): Result {
        val id = inputData.getLong(postKey, 0L)
        if (id == 0L) {
            return Result.failure()
        }
        return try {
            repository.processWork(id)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}