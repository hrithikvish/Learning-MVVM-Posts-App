package com.hrithikvish.mvvmpostsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrithikvish.mvvmpostsapp.model.postModel.Post
import com.hrithikvish.mvvmpostsapp.model.postModel.PostRequest
import com.hrithikvish.mvvmpostsapp.model.postModel.PostResponse
import com.hrithikvish.mvvmpostsapp.model.postModel.PostUpdateRequest
import com.hrithikvish.mvvmpostsapp.repository.PostRepository
import com.hrithikvish.mvvmpostsapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {

    val postResponseResponseLiveData: LiveData<NetworkResult<PostResponse>>
        get() = postRepository.getPostsResponseLiveData
    val updateDeletePostResponseLiveData: LiveData<NetworkResult<Post>>
        get() = postRepository.updateDeletePostResponseLiveData
    val addPostResponseLiveData: LiveData<NetworkResult<Post>>
        get() = postRepository.addPostResponseLiveData

    fun getPosts(
        limit: Int = 30,
        skip: Int = 0
    ) {
        viewModelScope.launch {
            postRepository.getPosts(limit, skip)
        }
    }

    fun addPost(postRequest: PostRequest) {
        viewModelScope.launch {
            postRepository.addPost(postRequest)
        }
    }

    fun updatePost(postUpdateRequest: PostUpdateRequest, postId: Int) {
        viewModelScope.launch {
            postRepository.updatePost(postUpdateRequest, postId)
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            postRepository.deletePost(postId)
        }
    }

}