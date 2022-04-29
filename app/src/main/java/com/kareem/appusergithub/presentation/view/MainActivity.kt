package com.kareem.appusergithub.presentation.view

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kareem.appusergithub.R
import com.kareem.appusergithub.presentation.adapter.GithubUserAdapter
import com.kareem.appusergithub.presentation.viewModel.ViewModelFactory
import com.kareem.appusergithub.databinding.ActivityMainBinding
import com.kareem.appusergithub.presentation.viewModel.MainViewModel
import com.kareem.appusergithub.utils.SettingsMode


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: GithubUserAdapter

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = obtainFactory(this as AppCompatActivity)

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvMain.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvMain.layoutManager = LinearLayoutManager(this)
        }
        adapter = GithubUserAdapter()
        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = this@MainActivity.adapter
        }

        themeMode()
        showTextDummy(true)
        binding.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query != null){
                        setData(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }


    }
    private fun obtainFactory(activity: AppCompatActivity): MainViewModel{
        val factory: ViewModelFactory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun themeMode() {
        val prefs = SettingsMode.getInstance(dataStore)
        val settingModel = ViewModelProvider(this, SettingViewModelFactory(prefs))[SettingViewModel::class.java]
        settingModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun showTextDummy(state: Boolean) {
        binding.dummyMain.isVisible = state
        binding.imgDummy.isVisible = state
    }


    private fun showLoading(loading: Boolean) {
        binding.apply {
            progressbar.visibility = if (loading) View.VISIBLE else View.GONE
            dummyMain.visibility = if(loading) View.INVISIBLE else View.GONE
            imgDummy.visibility = if(loading) View.INVISIBLE else View.GONE
            rvMain.isGone = loading

        }
    }
    private fun setData(query: String) {
        showLoading(true)
        mainViewModel.getSearch(query).observe(this) { list ->
            list.let {
                showTextDummy(false)
                showLoading(false)
                adapter.setData(it)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            R.id.action_mode -> {
                startActivity(Intent(this, ModeActivity::class.java))
                return true
            }
            R.id.action_bookmark -> {
                startActivity(Intent(this, StarActivity::class.java))
                return true
            }
            else -> return true
        }

    }


}