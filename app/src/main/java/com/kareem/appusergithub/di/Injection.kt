package com.kareem.appusergithub.di

import androidx.lifecycle.ViewModelProvider
import com.kareem.appusergithub.data.remote.ApiService
import com.kareem.appusergithub.presentation.repository.Repository
import com.kareem.appusergithub.presentation.viewModel.ViewModelFactory

object Injection {
    private fun provideGithubRepository(): Repository {
        return Repository(ApiService.create())
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideGithubRepository())
    }
}