package com.hrithikvish.mvvmpostsapp.model.postModel

data class PostUpdateRequest (
    val userId: Int?,
    val title: String?,
    val body: String?,
    val tags: List<String>?
)