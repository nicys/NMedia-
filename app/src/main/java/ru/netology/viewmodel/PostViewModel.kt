package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post
import ru.netology.model.FeedModel
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryImpl
import ru.netology.util.SingleLiveEvent

private val empty = Post(
    id = 0,
    author = "",
    authorAvatar = "",
    published = "",
    content = "",
    likeByMe = false,
    likes = 0,
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
    private val _networkError = SingleLiveEvent<String>()
    val networkError: LiveData<String>
    get() = _networkError

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.GetAllCallback {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun save() {
        edited.value?.let {
            repository.saveAsync(object : PostRepository.GetPostCallback {
                override fun onSuccess(post: Post) {
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    _networkError.value = e.message
                }
            }, it)
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
        repository.likeByIdAsync(object : PostRepository.GetPostCallback {
            override fun onSuccess(post: Post) {
                _data.postValue(
                    FeedModel(posts = _data.value?.posts
                        .orEmpty().map { if (it.id != post.id) post else it.copy(
                            likeByMe = !it.likeByMe,
                            likes = it.likes + 1
                        )
                        })
                )
            }

            override fun onError(e: Exception) {
                _networkError.value = e.message
            }
        }, id)
    }

//    fun unLikeById(id: Long) {
//        repository.likeByIdAsync(object : PostRepository.GetPostCallback {
//            override fun onSuccess(post: Post) {
//                _data.postValue(
//                    FeedModel(posts = _data.value?.posts
//                        .orEmpty().map { if (it.id != post.id) post else it.copy(
//                            likeByMe = !it.likeByMe,
//                            likes = it.likes - 1
//                        )
//                        })
//                )
//            }
//
//            override fun onError(e: Exception) {
//                _networkError.value = e.message
//            }
//        }, id)
//    }



    fun shareById(id: Long) {
        repository.shareByIdAsync(object : PostRepository.GetPostCallback {
            override fun onSuccess(post: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty().map {
                        if (it.id != id) it else it.copy(
                            sharesCnt = it.sharesCnt + 1,
                            shares = totalizerSmartFeed(it.sharesCnt + 1)
                        )
                    })
                )
            }

            override fun onError(e: Exception) {
                _networkError.value = e.message
            }
        }, id)
    }

    fun removeById(id: Long) {
        repository.removeByIdAsync(object : PostRepository.GetPostCallback {
            override fun onSuccess(post: Post) {
                val posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                _data.postValue(
                    _data.value?.copy(posts = posts, empty = posts.isEmpty())
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        }, id)
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