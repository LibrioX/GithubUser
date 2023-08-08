package com.example.githubuser.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivitySettingsBinding
import com.example.githubuser.viewModel.SettingsViewModel
import com.example.githubuser.viewModel.ViewModelFactory
import kotlin.math.log


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding

    private val settingsViewModel by viewModels<SettingsViewModel>{
        ViewModelFactory.getInstance(application, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.Settings)

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
                Log.d("TAG", "called 1")
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
                Log.d("TAG", "called 2")
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveThemeSetting(isChecked)
        }

    }
}