package com.kareem.appusergithub.presentation.repository

import com.kareem.appusergithub.data.model.SearchResult
import com.kareem.appusergithub.data.model.UserItems
import com.kareem.appusergithub.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import java.io.IOException


private const val GITHUB_STARTING_PAGE_INDEX = 1

class Repository(private val apiService: ApiService) {


    private val inMemoryCache = mutableListOf<UserItems>()
    private val searchResult = MutableSharedFlow<SearchResult>(replay = 1)
    private var lastRequestPage = GITHUB_STARTING_PAGE_INDEX
    private var isRequestInProgress = false

    companion object {
        private const val ITEM_PAGE = 100
    }

    suspend fun getSearchResultStream(query: String): Flow<SearchResult> {
        lastRequestPage = 1
        inMemoryCache.clear()
        requestSaveData(query)
        return searchResult
    }

    suspend fun requestMore(query: String) {
        if (isRequestInProgress) return
        val succesful = requestSaveData(query)
        if (succesful) {
            lastRequestPage++
        }
    }

    private suspend fun requestSaveData(query: String): Boolean {
        isRequestInProgress = true
        var successful = false
        val apiQuery = query
        try {
            val response = apiService.searchRepos(apiQuery, lastRequestPage, ITEM_PAGE)
            val reposs = response.items
            inMemoryCache.addAll(reposs)
            val responseByName = getUserByName(query)
            searchResult.emit(SearchResult.Success(responseByName))
            successful = true
        } catch (exception: IOException) {
            searchResult.emit(SearchResult.Error(exception))
        } catch (exception: HttpException) {
            searchResult.emit(SearchResult.Error(exception))
        }
        isRequestInProgress = false
        return successful
    }

    private fun getUserByName(query: String): List<UserItems> {
        return inMemoryCache.filter {
            it.login.contains(query, true) || (it.login.contains(query, true))
        }
    }


}

