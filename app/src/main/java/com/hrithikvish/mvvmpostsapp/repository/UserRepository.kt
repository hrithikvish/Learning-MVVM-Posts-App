package com.hrithikvish.mvvmpostsapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hrithikvish.mvvmpostsapp.api.UserAPI
import com.hrithikvish.mvvmpostsapp.model.userModel.UserRequest
import com.hrithikvish.mvvmpostsapp.model.userModel.UserResponse
import com.hrithikvish.mvvmpostsapp.util.Constants
import com.hrithikvish.mvvmpostsapp.util.NetworkResult
import com.hrithikvish.mvvmpostsapp.util.NetworkResultHelper
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userAPI: UserAPI
) {

    private var _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData : LiveData<NetworkResult<UserResponse>> get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signUp(userRequest)
        NetworkResultHelper.handleResponse(_userResponseLiveData, response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signIn(userRequest)
        NetworkResultHelper.handleResponse(_userResponseLiveData, response)
    }

}
