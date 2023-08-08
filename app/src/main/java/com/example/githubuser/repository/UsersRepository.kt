package com.example.githubuser.repository

import com.example.githubuser.api.ApiService
import com.example.githubuser.db.FavoriteUser
import com.example.githubuser.db.UserDao



class UsersRepository private constructor (
    private val userApi: ApiService,
    private val userDao: UserDao
    ) {

    suspend fun findUsers(query: String) = userApi.getUsers(query)

    suspend fun getDetailUser(username: String) = userApi.getDetailUser(username)

    suspend fun getFollowers(username: String) = userApi.getFollowers(username)

    suspend fun getFollowing(username: String) = userApi.getFollowing(username)

    suspend fun saveUser(user: FavoriteUser) = userDao.saveUser(user)

    suspend fun deleteUser(user: FavoriteUser) = userDao.deleteUser(user)

    fun getFavoriteUserByUsername(username: String) = userDao.getFavoriteUserByUsername(username)

    fun getAllFavoriteUser() = userDao.getAllFavoriteUser()


    companion object {
        @Volatile
        private var instance: UsersRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao
        ): UsersRepository =
            instance ?: synchronized(this) {
                instance ?: UsersRepository(apiService, userDao)
            }.also { instance = it }
    }

}