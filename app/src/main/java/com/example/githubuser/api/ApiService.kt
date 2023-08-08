package com.example.githubuser.api

import com.example.githubuser.models.ResponseUser
import com.example.githubuser.models.ResponseUserDetail
import com.example.githubuser.models.Users
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun getUsers(
        @Query("q")
        username : String
    ): ResponseUser

    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username: String): ResponseUserDetail

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): List<Users>
    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): List<Users>
}