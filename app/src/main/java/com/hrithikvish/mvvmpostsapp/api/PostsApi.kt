package com.hrithikvish.mvvmpostsapp.api

import com.hrithikvish.mvvmpostsapp.model.postModel.Post
import com.hrithikvish.mvvmpostsapp.model.postModel.PostRequest
import com.hrithikvish.mvvmpostsapp.model.postModel.PostResponse
import com.hrithikvish.mvvmpostsapp.model.postModel.PostUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PostsApi {

    @GET("/posts")
    suspend fun getPosts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): Response<PostResponse>

    @POST("/posts/add")
    suspend fun addPost(
        @Body postRequest: PostRequest
    ): Response<Post>

    @PUT("/posts/{postId}")
    suspend fun updatePost(
        @Path("postId") postId: Int,
        @Body postUpdateRequest: PostUpdateRequest
    ): Response<Post>

    @DELETE("/posts/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Int
    ): Response<Post>

}