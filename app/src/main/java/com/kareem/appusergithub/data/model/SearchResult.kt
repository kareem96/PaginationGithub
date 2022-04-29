package com.kareem.appusergithub.data.model

sealed class SearchResult {
    data class Success(val data: List<UserItems>): SearchResult()
    data class Error(val error: Exception): SearchResult()
}