//package ru.netology.repository
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import ru.netology.dto.Post
//import ru.netology.dao.PostDao
//
//class PostRepositorySQLiteImpl(
//    private val dao: PostDao
//) : PostRepository {
//    private var posts = emptyList<Post>()
//    private val data = MutableLiveData(posts)
//
//    init {
//        posts = dao.getAll()
//        data.value = posts
//    }
//
//    override fun getAll(): LiveData<List<Post>> = data
//
//    override fun save(post: Post) {
//        val id = post.id
//        val saved = dao.save(post)
//        posts = if (id == 0L) {
//            listOf(saved) + posts
//        } else {
//            posts.map {
//                if (it.id != id) it else saved
//            }
//        }
//        data.value = posts
//    }
//
//    override fun likeById(id: Long) {
//        dao.likeById(id)
//        posts = posts.map {
//            if (it.id != id) it else it.copy(
//                likeByMe = !it.likeByMe,
//                like = if (it.likeByMe) it.like - 1 else it.like + 1
//            )
//        }
//        data.value = posts
//    }
//
//    override fun removeById(id: Long) {
//        dao.removeById(id)
//        posts = posts.filter { it.id != id }
//        data.value = posts
//    }
//
//    override fun shareById(id: Long) {
//        dao.shareById(id)
//        posts = posts.map {
//            if (it.id != id) it else it.copy(
//                sharesCnt = it.sharesCnt + 1,
//                shares = totalizerSmartFeed(it.sharesCnt + 1)
//            )
//        }
//        data.value = posts
//    }
//
//    private fun counterOverThousand(feed: Int): Int {
//        return when (feed) {
//            in 1_000..999_999 -> feed / 100
//            else -> feed / 100_000
//        }
//    }
//
//    private fun totalizerSmartFeed(feed: Int): String {
//        return when (feed) {
//            in 0..999 -> "$feed"
//            in 1_000..999_999 -> "${(counterOverThousand(feed).toDouble() / 10)}K"
//            else -> "${(counterOverThousand(feed).toDouble() / 10)}M"
//        }
//    }
//}

