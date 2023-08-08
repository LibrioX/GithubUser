package com.example.githubuser.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.animation.ShimmerAnimation
import com.example.githubuser.databinding.ActivityUserDetailBinding
import com.example.githubuser.db.FavoriteUser
import com.example.githubuser.models.ResponseUserDetail
import com.example.githubuser.repository.Result
import com.example.githubuser.viewModel.ViewModelFactory
import com.example.githubuser.viewModel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private var avatarUrl : String = ""
    private var htmlLink : String = ""

    private val mainViewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(application, null)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0f

        val query = intent.getStringExtra(EXTRA_USERNAME) ?: ""

        //digunakan supaya tidak memanggil API lagi saat orientation berubah
        if(mainViewModel.getUsername() != query){
            mainViewModel.setUsername(query)
            mainViewModel.getUserDetail(query)
        }

        mainViewModel.userDetail.observe (this) { result ->
            when(result){
                is Result.Success -> {
                    showLoading(false)
                    setUserDetail(result.data)
                }
                is Result.Error -> {
                    showLoading(false)
                    toastMaker("Terjadi kesalahan : " + result.error)
                }
                is Result.Loading -> {
                    showLoading(true)
                }
            }
        }

        mainViewModel.getFavoriteUser().observe(this) { result ->

            binding.btnFavorite.setImageResource(if (result != null) R.drawable.baseline_favorite_fill
            else R.drawable.baseline_favorite_border)

            binding.btnFavorite.setOnClickListener {
                val username = binding.txtUsername.text.toString()
                val favoriteUser = FavoriteUser(username,avatarUrl)
                if(result != null){
                    mainViewModel.deleteUser(favoriteUser)
                    toastMaker("Berhasil menghapus $username sebagai favorite user")
                }else{
                    mainViewModel.saveUser(favoriteUser)
                    toastMaker("Berhasil menambahkan $username sebagai favorite user")
                }
            }
        }


        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = query

        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()




    }

    private fun setUserDetail(userDetail : ResponseUserDetail){
        avatarUrl = userDetail.avatarUrl
        htmlLink = userDetail.htmlUrl
        Glide.with(this@UserDetailActivity)
            .load(userDetail.avatarUrl)
            .placeholder(ShimmerAnimation.runShimmerAnimation())
            .into(binding.imgUser)
        binding.txtUsername.text = userDetail.login
        binding.txtName.text = userDetail.name ?: userDetail.login
        binding.jmlFollowers.text = userDetail.followers.toString()
        binding.jmlFollowing.text = userDetail.following.toString()
        supportActionBar?.title = userDetail.login
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutLoading.layoutAllLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun toastMaker(message : String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.share -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, htmlLink)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share to: "))
                true
            }
            R.id.webView -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(htmlLink)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    companion object {
        @StringRes
        val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )

        const val EXTRA_USERNAME = "username"
    }

}