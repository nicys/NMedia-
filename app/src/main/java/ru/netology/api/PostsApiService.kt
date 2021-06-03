package ru.netology.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.BuildConfig
import ru.netology.dto.Post

private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private val okhttp = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okhttp)
    .build()

interface PostsApiService {
    @GET("posts")
    suspend fun getAll(): Call<List<Post>>

    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: Long): Call<Post>

    @POST("posts")
    suspend fun save(@Body post: Post): Call<Post>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Call<Unit>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Call<Post>

    @DELETE("posts/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Long): Call<Post>

    @POST("posts/{id}/shares")
    suspend fun shareById(@Path("id") id: Long): Call<Post>
}

object PostsApi {
    val service: PostsApiService by lazy {
        retrofit.create(PostsApiService::class.java)
    }
}