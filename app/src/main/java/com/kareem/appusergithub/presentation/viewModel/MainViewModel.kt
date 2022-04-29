package com.kareem.appusergithub.presentation.viewModel


import android.app.Application
import androidx.lifecycle.*
import com.kareem.appusergithub.data.model.SearchResult
import com.kareem.appusergithub.presentation.repository.Repository

class MainViewModel(application:Application) : ViewModel() {
    private val repository = Repository(application)

    companion object{
        private const val VISIBLE_THRESHOLD = 3
    }
    private val queryLiveData = MutableLiveData<String>()
    var repoResult: LiveData<SearchResult> = queryLiveData.switchMap { query ->
        liveData {
            if(query.trim().isNotEmpty()){
                val repo = repository.getS
            }
        }
    }

    fun getSearch(query: String) =  repository.getSearch(query)
}