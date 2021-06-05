package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.db.AppDb
import ru.netology.dto.Post
import ru.netology.model.FeedModel
import ru.netology.model.FeedModelState
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryImpl
import ru.netology.util.SingleLiveEvent

private val empty = Post(
    id = 0,
    author = "",
    authorAvatar = "",
    published = "",
    content = "",
    likedByMe = false,
    likes = 0,
    shares = "0",
    sharesCnt = 0,
    video = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao())

    val data: LiveData<FeedModel> = repository.data.map(::FeedModel)
    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _networkError = SingleLiveEvent<String>()
    val networkError: LiveData<String>
        get() = _networkError

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
                    repository.save(it)
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
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
}



//
//    fun removeById(id: Long) {
//        repository.removeByIdAsyn(object : PostRepository.Callback<Unit> {
//            override fun onSuccess(value: Unit) {
//                val posts = _data.value?.posts.orEmpty()
//                    .filter { it.id != id }
//                _data.postValue(
//                    _data.value?.copy(posts = posts, empty = posts.isEmpty())
//                )
//            }
//
//            override fun onError(e: Exception) {
//                _networkError.value = e.message
//            }
//        }, id)
//    }