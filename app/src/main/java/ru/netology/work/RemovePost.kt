package ru.netology.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.netology.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@HiltWorker
class RemovePostWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: PostRepository,
) : CoroutineWorker(applicationContext, params) {
    companion object {
        const val removeKey = "ru.netology.work.RemovePostWorker"
    }

    override suspend fun doWork(): ListenableWorker.Result {
        val id = inputData.getLong(removeKey, 0L)
        if (id == 0L) {
            return ListenableWorker.Result.failure()
        }
        return try {
            repository.processWorkRemoved(id)
            ListenableWorker.Result.success()
        } catch (e: Exception) {
            ListenableWorker.Result.retry()
        }
    }
}

@Singleton
class RemovePostWorkerFactory @Inject constructor(
    private val repository: PostRepository,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when (workerClassName) {
        RemovePostWorker::class.java.name ->
            RemovePostWorker(appContext, workerParameters, repository)
        else ->
            null
    }
}