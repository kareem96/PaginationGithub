package com.kareem.appusergithub.presentation.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kareem.appusergithub.presentation.repository.Repository

class ViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(repository) as T


            else -> throw  IllegalArgumentException("Unknown ViewModel class" + modelClass.name)
        }
    }
}