package com.hrithikvish.mvvmpostsapp.model.postModel

data class PostRequest(
    val userId: Int?,
    val title: String?,
    val body: String?,
    val tags: List<String>? = null
)