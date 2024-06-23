package com.hrithikvish.mvvmpostsapp.di

import com.hrithikvish.mvvmpostsapp.api.PostsApi
import com.hrithikvish.mvvmpostsapp.api.UserAPI
import com.hrithikvish.mvvmpostsapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.USER_API_BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun providesUserApi(retrofit: Retrofit): UserAPI {
        return retrofit.create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesPostApi(retrofit: Retrofit): PostsApi {
        return retrofit.create(PostsApi::class.java)
    }

}