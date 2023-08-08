package com.example.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UsersAdapter
import com.example.githubuser.databinding.ActivityUserFavoriteBinding
import com.example.githubuser.models.Users
import com.example.githubuser.viewModel.MainViewModel
import com.example.githubuser.viewModel.ViewModelFactory

class UserFavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserFavoriteBinding

    private val mainViewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(application, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.FavoriteUser)

        mainViewModel.getAllFavoriteUser().observe(this) { users ->
            binding.nullUser.visibility =  if(users.isEmpty()) View.VISIBLE else View.GONE
            val items = arrayListOf<Users>()
            users.map {
                val item = Users(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            binding.rvUser.layoutManager = LinearLayoutManager(this)
            binding.rvUser.adapter = UsersAdapter(items)
        }
    }
}