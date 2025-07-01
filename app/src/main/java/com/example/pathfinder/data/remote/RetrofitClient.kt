package com.example.pathfinder.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://N2Y7YGCWJZ-dsn.algolia.net/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val algoliaApi: AlgoliaApi = retrofit.create(AlgoliaApi::class.java)
}