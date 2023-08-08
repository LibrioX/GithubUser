package com.example.githubuser.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.db.FavoriteUser
import com.example.githubuser.models.ResponseUserDetail
import com.example.githubuser.models.Users
import com.example.githubuser.repository.UsersRepository
import kotlinx.coroutines.launch
import com.example.githubuser.repository.Result


class MainViewModel(private val usersRepository: UsersRepository) : ViewModel() {

    private val _users = MutableLiveData<Result<List<Users>>>()
    val users: LiveData<Result<List<Users>>> = _users

    private val _userDetail = MutableLiveData<Result<ResponseUserDetail>>()
    val userDetail: LiveData<Result<ResponseUserDetail>> = _userDetail

    private val _follow = MutableLiveData<Result<List<Users>>>()
    val follow: LiveData<Result<List<Users>>> = _follow

    private val _username = MutableLiveData<String>()

    fun setUsername(username: String){
        _username.value = username
    }

    fun getUsername() = _username.value

    // Find Users
    fun findUsers(query: String) = viewModelScope.launch {
        callFindUsers(query)
    }

    private suspend fun callFindUsers(query: String){
        try {
            _users.postValue(Result.Loading)
            val response = usersRepository.findUsers(query)
            _users.postValue(Result.Success(response.items))
        }catch (e: Exception){
            _users.postValue(Result.Error(e.message.toString()))
        }
    }

    // Get Detail User
    fun getUserDetail(username: String) = viewModelScope.launch {
        callGetUserDetail(username)
    }

    private suspend fun callGetUserDetail(username: String){
        try{
            _userDetail.postValue(Result.Loading)
            val response = usersRepository.getDetailUser(username)
            _userDetail.postValue(Result.Success(response))
        }catch (e: Exception){
            _userDetail.postValue(Result.Error(e.message.toString()))
        }
    }

    // get Followers
    fun getFollowers(username: String) = viewModelScope.launch {
        callGetFollowers(username)
    }

    private suspend fun  callGetFollowers(username: String){
        try{
            _follow.postValue(Result.Loading)
            val response = usersRepository.getFollowers(username)
            _follow.postValue(Result.Success(response))
        }catch (e: Exception){
            _follow.postValue(Result.Error(e.message.toString()))
        }
    }

    // get Following
    fun getFollowing(username: String) = viewModelScope.launch {
        callGetFollowing(username)
    }

    private suspend fun  callGetFollowing(username: String){
        try{
            _follow.postValue(Result.Loading)
            val response = usersRepository.getFollowing(username)
            _follow.postValue(Result.Success(response))
        }catch (e: Exception){
            _follow.postValue(Result.Error(e.message.toString()))
        }
    }

    // save user
    fun saveUser(user: FavoriteUser) = viewModelScope.launch {
        usersRepository.saveUser(user)
    }

    // delte user
    fun deleteUser(user: FavoriteUser) = viewModelScope.launch {
        usersRepository.deleteUser(user)
    }

    //get Favorite User
    fun getFavoriteUser() = usersRepository.getFavoriteUserByUsername(_username.value.toString())

    //get all user
    fun getAllFavoriteUser() = usersRepository.getAllFavoriteUser()




}