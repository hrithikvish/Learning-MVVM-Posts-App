package com.hrithikvish.mvvmpostsapp.util

import androidx.lifecycle.MutableLiveData
import org.json.JSONObject
import retrofit2.Response

object NetworkResultHelper {

    fun <T> handleResponse(
        responseLiveData: MutableLiveData<NetworkResult<T>>,
        response: Response<T>,
        operation: Operation? = null
    ) {
        if (response.isSuccessful && response.body() != null) {
            responseLiveData.postValue(NetworkResult.Success(response.body()!!, operation))
        }
        else if (response.errorBody() != null) {
            val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
            responseLiveData.postValue(NetworkResult.Error(
                message = errorBody.getString("message")
            ))
        }
        else {
            responseLiveData.postValue(NetworkResult.Error(
                message = "Something went wrong"
            ))
        }
    }
}