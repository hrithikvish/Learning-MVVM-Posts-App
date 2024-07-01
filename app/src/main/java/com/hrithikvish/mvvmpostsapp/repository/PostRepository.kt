package com.hrithikvish.mvvmpostsapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hrithikvish.mvvmpostsapp.api.PostsApi
import com.hrithikvish.mvvmpostsapp.model.postModel.Post
import com.hrithikvish.mvvmpostsapp.model.postModel.PostRequest
import com.hrithikvish.mvvmpostsapp.model.postModel.PostResponse
import com.hrithikvish.mvvmpostsapp.model.postModel.PostUpdateRequest
import com.hrithikvish.mvvmpostsapp.util.Constants
import com.hrithikvish.mvvmpostsapp.util.NetworkResult
import com.hrithikvish.mvvmpostsapp.util.NetworkResultHelper
import com.hrithikvish.mvvmpostsapp.util.Operation
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postsApi: PostsApi,
) {

    private var _getPostsResponseLiveData = MutableLiveData<NetworkResult<PostResponse>>()
    val getPostsResponseLiveData: LiveData<NetworkResult<PostResponse>> get() = _getPostsResponseLiveData

    private var _updateDeletePostResponseLiveData = MutableLiveData<NetworkResult<Post>>()
    val updateDeletePostResponseLiveData: LiveData<NetworkResult<Post>> get() = _updateDeletePostResponseLiveData

    private var _addPostResponseLiveData = MutableLiveData<NetworkResult<Post>>()
    val addPostResponseLiveData: LiveData<NetworkResult<Post>> get() = _addPostResponseLiveData

    suspend fun getPosts(limit: Int, skip: Int) {
        if(_getPostsResponseLiveData.value?.data == null) {
            _getPostsResponseLiveData.postValue(NetworkResult.Loading())
            val response = postsApi.getPosts(limit = limit, skip = skip)
            NetworkResultHelper.handleResponse(_getPostsResponseLiveData, response)
        }
    }

    suspend fun addPost(postRequest: PostRequest) {
        _addPostResponseLiveData.postValue(NetworkResult.Loading())
        val response = postsApi.addPost(postRequest)
        Log.d(Constants.TAG, "addPost: ${response.body()}")
        NetworkResultHelper.handleResponse(_addPostResponseLiveData, response)
    }

    suspend fun updatePost(postUpdateRequest: PostUpdateRequest, postId: Int) {
        _updateDeletePostResponseLiveData.postValue(NetworkResult.Loading())
        val response = postsApi.updatePost(postId, postUpdateRequest)
        Log.d(Constants.TAG, "updatePost: ${response.body()}")
        NetworkResultHelper.handleResponse(_updateDeletePostResponseLiveData, response, Operation.UPDATE_POST)
    }

    suspend fun deletePost(postId: Int) {
        _updateDeletePostResponseLiveData.postValue(NetworkResult.Loading())
        val response = postsApi.deletePost(postId)
        Log.d(Constants.TAG, "deletePost: ${response.body()}")
        NetworkResultHelper.handleResponse(_updateDeletePostResponseLiveData, response, Operation.DELETE_POST)
    }

}