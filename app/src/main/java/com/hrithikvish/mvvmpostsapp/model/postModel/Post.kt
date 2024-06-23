package com.hrithikvish.mvvmpostsapp.model.postModel

data class Post(
    val id: Int?,
    val userId: Int?,
    val title: String?,
    val body: String?,
    val tags: List<String?>?,
    val reactions: Reactions?,
    val views: Int?,
    val isDeleted: Boolean?,
    val deletedOn: String?
)