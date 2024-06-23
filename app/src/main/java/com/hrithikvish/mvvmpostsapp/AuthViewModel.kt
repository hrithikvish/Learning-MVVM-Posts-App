package com.hrithikvish.mvvmpostsapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrithikvish.mvvmpostsapp.model.userModel.UserRequest
import com.hrithikvish.mvvmpostsapp.model.userModel.UserResponse
import com.hrithikvish.mvvmpostsapp.repository.UserRepository
import com.hrithikvish.mvvmpostsapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>> get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

}