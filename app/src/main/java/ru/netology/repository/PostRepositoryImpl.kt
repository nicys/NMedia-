package ru.netology.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.api.PostsApi
import ru.netology.dto.Post


class PostRepositoryImpl : PostRepository {

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(
                call: Call<List<Post>>,
                response: Response<List<Post>>
            ) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(
                    response.body() ?: throw java.lang.RuntimeException("body is null")
                )
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }

    override fun saveAsyn(callback: PostRepository.Callback<Post>, post: Post) {
        PostsApi.retrofitService.save(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(
                    response.body() ?: throw RuntimeException("body is null")
                )
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }


    override fun likeByIdAsyn(callback: PostRepository.Callback<Post>, id: Long) {
        PostsApi.retrofitService.likeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(
                    response.body() ?: throw RuntimeException("body is null")
                )
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }

    override fun dislikeByIdAsyn(callback: PostRepository.Callback<Post>, id: Long) {
        PostsApi.retrofitService.dislikeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(
                    response.body() ?: throw RuntimeException("body is null")
                )
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }

    override fun shareByIdAsyn(callback: PostRepository.Callback<Post>, id: Long) {
        PostsApi.retrofitService.shareById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(
                    response.body() ?: throw RuntimeException("body is null")
                )
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }

    override fun removeByIdAsyn(callback: PostRepository.Callback<Unit>, id: Long) {
        PostsApi.retrofitService.removeById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(
                    response.body() ?: throw RuntimeException("body is null")
                )
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }
}