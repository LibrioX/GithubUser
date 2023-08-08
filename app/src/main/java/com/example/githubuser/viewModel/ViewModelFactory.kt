package com.example.githubuser.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.di.Injection
import com.example.githubuser.preferences.SettingPreferences
import com.example.githubuser.repository.UsersRepository


class ViewModelFactory private constructor(private val usersRepository: UsersRepository, private val pref: SettingPreferences?) : ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(usersRepository) as T
        }else if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return pref?.let { SettingsViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, pref: SettingPreferences? ): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context),pref)
            }.also { instance = it }
    }
}