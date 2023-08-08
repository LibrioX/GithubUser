package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.api.ApiConfig
import com.example.githubuser.db.UsersDatabase
import com.example.githubuser.repository.UsersRepository

object Injection {
    fun provideRepository(context: Context): UsersRepository {
        val apiService = ApiConfig.getApiService()
        val database = UsersDatabase.getInstance(context)
        val dao = database.userDao()
        return UsersRepository.getInstance(apiService, dao)
    }
}