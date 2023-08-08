package com.example.githubuser.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface UserDao {
    @Insert
    suspend fun saveUser(user: FavoriteUser)

    @Delete
    suspend fun deleteUser(user: FavoriteUser)

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    @Query("SELECT * FROM FavoriteUser")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

}