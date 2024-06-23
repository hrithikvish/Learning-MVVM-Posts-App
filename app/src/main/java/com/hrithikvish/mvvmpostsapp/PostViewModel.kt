package com.hrithikvish.mvvmpostsapp

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
    val addUpdateDeletePostResponseLiveData: LiveData<NetworkResult<Post>>
        get() = postRepository.addUpdateDeletePostResponseLiveData

    fun getPosts(limit: Int, skip: Int) {
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


}