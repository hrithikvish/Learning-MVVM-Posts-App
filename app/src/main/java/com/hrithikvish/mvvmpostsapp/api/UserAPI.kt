package com.hrithikvish.mvvmpostsapp.api

import com.hrithikvish.mvvmpostsapp.model.userModel.UserRequest
import com.hrithikvish.mvvmpostsapp.model.userModel.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("/users/add")
    suspend fun signUp(@Body userRequest: UserRequest): Response<UserResponse>

    @POST("/auth/login")
    suspend fun signIn(@Body userRequest: UserRequest): Response<UserResponse>

}