package ru.netology.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.auth.AppAuth
import ru.netology.db.AppDb
import ru.netology.dto.MediaUpload
import ru.netology.dto.Post
import ru.netology.model.FeedModel
import ru.netology.model.FeedModelState
import ru.netology.model.PhotoModel
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryImpl
import ru.netology.util.SingleLiveEvent
import java.io.File

private val empty = Post(
    id = 0,
    author = "",
    authorId = 0,
    authorAvatar = "",
    published = "",
    content = "",
    likedByMe = false,
    likes = 0,
    shares = "0",
    sharesCnt = 0,
    video = null,
    attachment = null,
)

private val noPhoto = PhotoModel()

@ExperimentalCoroutinesApi
class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao())

    val data: LiveData<FeedModel> = AppAuth.getInstance()
        .authStateFlow
        .flatMapLatest { (myId, _) ->
            repository.data
                .map { posts ->
                    FeedModel(
                        posts.map { it.copy(ownedByMe = it.authorId == myId) },
                        posts.isEmpty()
                    )
                }
        }.asLiveData(Dispatchers.Default) /* т.к. это переменная, то используем оператор приведения типа asLiveData
        На самом деле это библиотечный extension*/

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    // data - наши посты, если они обновляются, то включается switchMap, которорый запускает, блок в {}
    val newerCount: LiveData<Int> = data.switchMap {
        repository.getNewerCount(it.posts.firstOrNull()?.id ?: 0L)
            .catch { e -> e.printStackTrace() }
            .asLiveData()
    }

    private val _networkError = SingleLiveEvent<String>()
    val networkError: LiveData<String>
        get() = _networkError

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    init {
        loadPosts()
    }

    /* Используем viewModelScope (расширяется от MainScope, который в свою очередь расширяется от CoroutineScope)
    для запуска корутины, т.к. она уже интегрирована с ViewModel и не нужно переопределять метод onClear() на закрытие корутины */
    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun save() {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    when (_photo.value) {
                        noPhoto -> repository.save(it)
                        else -> _photo.value?.file?.let { file ->
                            repository.saveWithAttachment(it, MediaUpload(file))
                        }
                    }
                    _dataState.value = FeedModelState()
                    edited.value = empty
                    _photo.value = noPhoto
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
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

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun likeById(id: Long) {
        viewModelScope.launch {
            try {
                repository.likeById(id)
                data.map {
                    FeedModel(posts = data.value?.posts
                        .orEmpty().map {
                            if (it.id != id) it else it.copy(
                                likedByMe = !it.likedByMe,
                                likes = it.likes + 1
                            )
                        })
                }
            } catch (e: Exception) {
                _networkError.value = e.message
            }
        }
    }

//    fun unlikeById(id: Long) {
//        viewModelScope.launch {
//            try {
//                repository.unlikeById(id)
//                data.map {
//                    FeedModel(posts = data.value?.posts
//                        .orEmpty().map {
//                            if (it.id != id) it else it.copy(
//                                likedByMe = !it.likedByMe,
//                                likes = it.likes - 1
//                            )
//                        })
//                }
//            } catch (e: Exception) {
//                _networkError.value = e.message
//            }
//        }
//    }


    fun shareById(id: Long) {
        viewModelScope.launch {
            try {
                repository.shareById(id)
                data.map {
                    FeedModel(posts = data.value?.posts
                        .orEmpty().map {
                            if (it.id != id) it else it.copy(
                                sharesCnt = it.sharesCnt + 1,
                                shares = totalizerSmartFeed(it.sharesCnt + 1)
                            )
                        })
                }
            } catch (e: Exception) {
                _networkError.value = e.message
            }
        }
    }

    fun removeById(id: Long) {
        viewModelScope.launch {
            try {
                repository.removeById(id)
                val posts = data.value?.posts.orEmpty()
                    .filter { it.id != id }
                data.value?.copy(posts = posts, empty = posts.isEmpty())
            } catch (e: Exception) {
                _networkError.value = e.message
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

    fun getPostById(id: Long): LiveData<FeedModel> = data.map { FeedModel(posts = data.value?.posts
        .orEmpty().map {
            if (it.id == id) it else empty
        })
    }
}