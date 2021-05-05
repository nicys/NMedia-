package ru.netology.viewmodel

import android.app.Application
import android.service.controls.actions.BooleanAction
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.dto.Post
import ru.netology.model.FeedModel
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryImpl
import ru.netology.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    author = "",
    published = "",
    content = "",
    likeByMe = false,
    like = 0,
    shares = "0",
    sharesCnt = 0,
    video = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        thread {
            // Начинаем загрузку
            _data.postValue(FeedModel(loading = true))
            try {
                // Данные успешно получены
                val posts = repository.getAll()
                FeedModel(postS = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                // Получена ошибка
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }

    fun save() {
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) {
//        thread { repository.likeById(id) }


            _data.postValue(
                _data.value?.copy(postS = _data.value?.postS.orEmpty()
                    .filter  { it.id == id && !it.likeByMe }
                )
            )
             thread { repository.likeById(id) }
    }

    fun shareById(id: Long) {
        thread { repository.shareById(id) }
    }

    fun removeById(id: Long) {
        thread {
            // Оптимистичная модель
            val old = _data.value?.postS.orEmpty()
            _data.postValue(
                _data.value?.copy(postS = _data.value?.postS.orEmpty()
                    .filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(postS = old))
            }
        }
    }

    private fun counterOverThousand(feed: Int): Int {
        return when (feed) {
            in 1_000..999_999 -> feed / 100
            else -> feed / 100_000
        }
    }

    fun totalizerSmartFeed(feed: Int): String {
        return when (feed) {
            in 0..999 -> "$feed"
            in 1_000..999_999 -> "${(counterOverThousand(feed).toDouble() / 10)}K"
            else -> "${(counterOverThousand(feed).toDouble() / 10)}M"
        }
    }

    fun getPostById(id: Long): LiveData<Post?> = data.map { posts ->
        posts.postS.find {
            it.id == id
        }
    }

    fun List<Post>.isPostById(id: Long): Post = this.single { it.id == id }
}








//class PostViewModel(application: Application) : AndroidViewModel(application) {
//    // упрощённый вариант
//    private val repository: PostRepository = PostRepositoryRoomImpl(
//        AppDb.getInstance(context = application).postDao()
//    )
//    val data = repository.getAll()
//
//    val edited = MutableLiveData(empty)
//
//    fun save() {
//        edited.value?.let {
//            repository.save(it)
//        }
//        edited.value = empty
//    }
//
//    fun edit(post: Post) {
//        edited.value = post
//    }
//
//    fun changeContent(content: String) {
//        val text = content.trim()
//        if (edited.value?.content == text) {
//            return
//        }
//        edited.value = edited.value?.copy(content = text)
//    }
//
//    fun likeById(id: Long) = repository.likeById(id)
//    fun shareById(id: Long) = repository.shareById(id)
//    fun removeById(id: Long) = repository.removeById(id)






