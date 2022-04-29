package com.kareem.appusergithub.data.remote


import com.kareem.appusergithub.data.response.DetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users/{username}")
    @Headers("Accept:application/json")
    fun getSearchUser(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Call<DetailResponse>


}