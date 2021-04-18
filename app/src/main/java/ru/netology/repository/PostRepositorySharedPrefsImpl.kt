package ru.netology.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.dto.Post

class PostRepositorySharedPrefsImpl(
        context: Context,
) : PostRepository {
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val key = "posts"
    private var nextId = 1L
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            sync()
        }
    private val data = MutableLiveData(posts)

    init {
        prefs.getString(key, null)?.let {
            posts = gson.fromJson(it, type)
            nextId = posts.maxOf { post -> post.id } + 1
            data.value = posts
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(likeByMe = !it.likeByMe)
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(sharesCnt = it.sharesCnt + 1, shares = totalizerSmartFeed(it.sharesCnt + 1))
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            // TODO: remove hardcoded author & published
            posts = listOf(
                    post.copy(
                            id = nextId++,
                            author = "Me",
                            likeByMe = false,
                            published = "now",
                            video = null
                    )
            ) + posts
            data.value = posts
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }
    private fun sync() {
        with(prefs.edit()) {
            putString(key, gson.toJson(posts))
            apply()
        }
    }
}

fun counterOverThousand(feed: Int): Int {
    return when(feed) {
        in 1_000..999_999 -> feed/100
        else -> feed/100_000
    }
}

fun totalizerSmartFeed(feed: Int): String {
    return when(feed) {
        in 0..999 -> "$feed"
        in 1_000..999_999 -> "${ (counterOverThousand(feed).toDouble() / 10) }K"
        else -> "${ (counterOverThousand(feed).toDouble() / 10) }M"
    }
}