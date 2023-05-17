package com.example.shoppingtesting.data.remote

import com.example.shoppingtesting.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixaApi {

    @GET("/api/")
    suspend fun searchImage(
        @Query("q") searchQuery: String,
        @Query("api_key") api_key: String = BuildConfig.API_KEY
    ): Response<PixaData>
}