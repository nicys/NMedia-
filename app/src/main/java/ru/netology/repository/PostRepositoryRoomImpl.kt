package ru.netology.repository

//import androidx.lifecycle.Transformations
//import ru.netology.dao.PostDao
//import ru.netology.dto.Post
//import ru.netology.entity.PostEntity
//
//class PostRepositoryRoomImpl(
//    private val dao: PostDao,
//) : PostRepository {
//    override fun getAll() = Transformations.map(dao.getAll()) { list ->
//        list.map {
//            Post(it.id, it.author, it.content, it.published, it.likeByMe, it.like, it.shares, it.sharesCnt, it.video)
//        }
//    }
//
//    override fun likeById(id: Long) {
//        dao.likeById(id)
//    }
//
//    override fun shareById(id: Long) {
//        dao.shareById(id)
//    }
//
//    override fun save(post: Post) {
//        dao.save(PostEntity.fromDto(post))
//    }
//
//    override fun removeById(id: Long) {
//        dao.removeById(id)
//    }
//}