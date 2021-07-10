package ru.netology.work

import androidx.work.DelegatingWorkerFactory
import ru.netology.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DependencyWorkerFactory @Inject constructor(
    repository: PostRepository,
) : DelegatingWorkerFactory() {
    init {
        addFactory(RefreshPostsWorkerFactory(repository))
        addFactory(SavePostsWorkerFactory(repository))
        addFactory(RemovePostWorkerFactory(repository))
    }
}