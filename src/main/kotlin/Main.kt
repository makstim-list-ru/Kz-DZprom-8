package ru.netology

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import ru.netology.dto.PostWithCommentsAuthor
import ru.netology.retrofit.PostsRetrofit
import kotlin.coroutines.EmptyCoroutineContext

fun main() {
    val name = "Kotlin"
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, " + name + "!")

    val coroutine = CoroutineScope(EmptyCoroutineContext)

    coroutine.launch() {
        println(Thread.currentThread().name)


        val posts = PostsRetrofit.retrofitService.getAllPosts()
            .map { post ->
                async {
                    PostWithCommentsAuthor(
                        post = post,
                        comments = run {
                            val response = PostsRetrofit.retrofitService.getByIdComments(post.id)
                            if (response.isSuccessful) {
                                println("++++++++++++++++++++++++++++++++++" + true)
                                response.body()
                            } else {
                                println("__________________________________" + false)
                                null
                            }
                        },
                        author = PostsRetrofit.retrofitService.getByIdAuthors(post.authorId)
                    )
                }
            }.awaitAll()

        for (post in posts) println(post.comments)
        println()
        for (post in posts) println(post)

    }
    Thread.sleep(30_000L)


}