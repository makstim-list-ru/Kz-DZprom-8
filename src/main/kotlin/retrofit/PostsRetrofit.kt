package ru.netology.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import ru.netology.dto.Author
import ru.netology.dto.Comment
import ru.netology.dto.Post

interface PostsRetrofitInterface {
    @GET("posts")
    suspend fun getAllPosts(): List<Post>

    @GET("authors/{id}")
    suspend fun getByIdAuthors(@Path("id") id: Long): Author

    @GET("posts/{id}/comments")
    suspend fun getByIdComments(@Path("id") id: Long): Response<List<Comment>>
}

object PostsRetrofit {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okhttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private const val BASE_URL = "http://127.0.0.1:9999/api/"

    val retrofitService: PostsRetrofitInterface by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okhttp)
            .build()
            .create(PostsRetrofitInterface::class.java)
    }
}