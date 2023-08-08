package com.example.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.activity.SettingsActivity
import com.example.githubuser.activity.UserFavoriteActivity
import com.example.githubuser.adapter.UsersAdapter
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.models.Users
import com.example.githubuser.preferences.SettingPreferences
import com.example.githubuser.viewModel.ViewModelFactory
import com.example.githubuser.viewModel.MainViewModel
import com.example.githubuser.repository.Result
import com.example.githubuser.viewModel.SettingsViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(application,null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = SettingPreferences.getInstance(dataStore)

        val settingsViewModel by viewModels<SettingsViewModel>{
            ViewModelFactory.getInstance(application,pref)
        }

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.users.observe(this){ result ->
            when(result){
                is Result.Success -> {
                    showLoading(false)
                    setUsersData(result.data)
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        this,
                        "Terjadi kesalahan" + result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Result.Loading -> {
                    showLoading(true)
                }
            }

        }


    }

    private fun setUsersData (users : List<Users>){
        val adapter = UsersAdapter(users)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter
        binding.landingText.visibility = View.INVISIBLE
    }

    private fun showLoading(isLoading: Boolean) {
         binding.layoutLoading.layoutAllLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findUsers(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isNotEmpty()){
                    mainViewModel.findUsers(newText)
                }
                return true
            }


        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.favorite -> {
                startActivity(Intent(this, UserFavoriteActivity::class.java))
                true
            }
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }

}