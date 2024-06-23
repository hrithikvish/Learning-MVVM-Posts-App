package com.hrithikvish.mvvmpostsapp.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserIdManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private var pref = context.getSharedPreferences(Constants.PREF_USER_ID, Context.MODE_PRIVATE)

    fun saveUserId(userId: Int) {
        val editor = pref.edit()
        editor.putInt(Constants.USER_ID, userId)
        editor.apply()
    }

    fun getUserId(): Int {
        return pref.getInt(Constants.USER_ID, -1)
    }

}