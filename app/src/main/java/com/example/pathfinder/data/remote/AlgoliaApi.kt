package com.example.pathfinder.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.Call

interface AlgoliaApi {

    @POST("1/indexes/{indexName}/query")
    fun searchJobs(
        @Path("indexName") indexName: String,
        @Header("X-Algolia-Application-Id") applicationId: String,
        @Header("X-Algolia-API-Key") apiKey: String,
        @Body query: Map<String, String> // Cập nhật tham số này để chỉ nhận String
    ): Call<AlgoliaSearchResponse>
}