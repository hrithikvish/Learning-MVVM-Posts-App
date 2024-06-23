package com.hrithikvish.mvvmpostsapp.util

import android.text.TextUtils

object ValidationUtil {
    fun validateCredential(username: String, password: String): Pair<Boolean, String> {
        var result = Pair(true, "")
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            result = Pair(false, "Please provide the credentials")
        } else if (password.length < 4)  {
            result = Pair(false, "Password must be at least 4 digits long")
        }
        return result
    }
}