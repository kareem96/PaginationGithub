package com.kareem.appusergithub.presentation.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kareem.appusergithub.data.model.SearchResult
import com.kareem.appusergithub.data.model.UserItems
import com.kareem.appusergithub.data.remote.ApiConfig
import com.kareem.appusergithub.data.response.SearchResponse
import com.kareem.appusergithub.data.remote.ApiService
import com.kareem.appusergithub.data.response.DetailResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val GITHUB_STARTING_PAGE_INDEX = 1

class Repository (application: Application) {

    private val apiService: ApiService = ApiConfig.getApiService()

    private val inMemoryCache = mutableListOf<UserItems>()
    private val searchResult = MutableSharedFlow<SearchResult>(replay = 1)
    private var lastRequestPage = GITHUB_STARTING_PAGE_INDEX
    private var isRequestInProgress = false
    companion object{
        private const val ITEM_PAGE = 100
    }
    suspend fun getSearchResultStream(query: String): Flow<SearchResult> {
        lastRequestPage = 1
        inMemoryCache.clear()
        requestSaveData(query)
        return searchResult
    }

    suspend fun requestMore(query: String){
        if(isRequestInProgress) return
        val succesful = requestSaveData(query)
        if(succesful){
            lastRequestPage++
        }
    }

    private suspend fun requestSaveData(query: String): Boolean {
        isRequestInProgress = true
        var successful = false
        val apiQuery = query
        try {
            val response = apiService.getSearchUser(apiQuery, lastRequestPage, ITEM_PAGE)
            val reposs = response.ite
        }
    }


    ///
    private val _searchUser = MutableLiveData<ArrayList<UserItems>>()
    val searchUser: LiveData<ArrayList<UserItems>> = _searchUser
    fun getSearch(query: String): LiveData<ArrayList<UserItems>> {
        val searchList = MutableLiveData<ArrayList<UserItems>>()
        val retrofit = apiService.searchUser(query)
        retrofit.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    searchList.postValue(response.body()?.items)

                } else {
                    val message = when (response.code()){
                        401 -> "${response.code()} : Forbidden"
                        403 -> "${response.code()} : Bad Request"
                        404 -> "${response.code()} : Not Found"
                        else -> "${response.code()} : ${response.body()}"
                    }
                    Log.d("MainViewModel", "onResponse: $message")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.d("MainViewModel", "onFailure: ${t.message.toString()}")
            }
        })
        return searchList
    }


}

