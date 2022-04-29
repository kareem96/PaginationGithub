package com.kareem.appusergithub.presentation.view

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kareem.appusergithub.presentation.adapter.GithubUserAdapter
import com.kareem.appusergithub.databinding.ActivityMainBinding
import com.kareem.appusergithub.di.Injection
import com.kareem.appusergithub.presentation.viewModel.MainViewModel
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.kareem.appusergithub.data.model.SearchResult


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private val adapter = GithubUserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvMain.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvMain.layoutManager = LinearLayoutManager(this)
        }

        mainViewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory()
        )[MainViewModel::class.java]

        setupScrollListener()
        val query = ""

        initAdapter()
        initSearch(query)
        showTextDummy(true)

    }

    private fun setupScrollListener() {
        val layoutManager = binding.rvMain.layoutManager as LinearLayoutManager
        binding.rvMain.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                mainViewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }


    private fun updateRepoListFromInput() {
        binding.searchView.query.trim().let {
            if (it.isNotEmpty()) {
                binding.rvMain.scrollToPosition(0)
                mainViewModel.getSearch(it.toString())
            }
        }
    }

    private fun initSearch(query: String) {
        binding.searchView.setQuery(query, true)
        binding.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        updateRepoListFromInput()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun initAdapter() {
        binding.rvMain.adapter = adapter
        mainViewModel.repoResult.observe(this) { result ->
            when (result) {
                is SearchResult.Success -> {
                    adapter.submitList(result.data)
                    showLoading(result.data.isEmpty())
                }
                is SearchResult.Error -> {
                    if (result.error.message.toString().isNotEmpty()) {
                        if (binding.searchView.query.trim().isNotEmpty()) {
                            var errorMessage: String = ""
                            if (result.error.localizedMessage.toString().contains("Failed to connect"))
                                errorMessage = "Not connected to internet"
                            else if (result.error.localizedMessage.toString().contains("Time Out"))
                                errorMessage = "No Connected to internet"
                            else if (result.error.localizedMessage.toString().contains("HTTP 403"))
                                errorMessage = ""
                        }
                    }
                }
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
            dummyMain.visibility = if (loading) View.INVISIBLE else View.GONE
            imgDummy.visibility = if (loading) View.INVISIBLE else View.GONE

        }
    }

}