package com.hrithikvish.mvvmpostsapp.model.postModel

data class PostResponse(
    val limit: Int?,
    val posts: List<Post?>?,
    val skip: Int?,
    val total: Int?
)