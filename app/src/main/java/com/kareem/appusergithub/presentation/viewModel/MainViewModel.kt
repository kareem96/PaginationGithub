package com.kareem.appusergithub.presentation.viewModel

import androidx.lifecycle.*
import com.kareem.appusergithub.data.model.SearchResult
import com.kareem.appusergithub.presentation.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository :Repository) : ViewModel() {


    companion object{
        private const val VISIBLE_THRESHOLD = 3
    }
    private val queryLiveData = MutableLiveData<String>()
    var repoResult: LiveData<SearchResult> = queryLiveData.switchMap { query ->
        liveData {
            if(query.trim().isNotEmpty()){
                val repo = repository.getSearchResultStream(query).asLiveData(Dispatchers.Main)
                emitSource(repo)
            }
        }
    }

    /*fun getSearch(query: String) =  repository.getSearch(query)*/
    fun getSearch(query: String) = queryLiveData.postValue(query)

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int){
        if(visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount){
            val immutableQuery = queryLiveData.value
            if(immutableQuery != null){
                viewModelScope.launch {
                    repository.requestMore(immutableQuery)
                }
            }
        }
    }
}