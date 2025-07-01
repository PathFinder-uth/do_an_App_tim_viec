package com.example.pathfinder.data.remote

import com.example.pathfinder.data.model.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlgoliaSearchService {

    fun searchJobs(query: String, onResult: (List<Job>) -> Unit, onError: (Throwable) -> Unit) {
        val queryMap = mapOf("query" to query)
        val applicationId = "N2Y7YGCWJZ" // Thay bằng Application ID của bạn
        val apiKey = "e9ec53637934ab9b43aeaea919dc699e" // Thay bằng API Key của bạn

        RetrofitClient.algoliaApi.searchJobs(
            indexName = "jobs", // Tên index trong Algolia
            applicationId = applicationId,
            apiKey = apiKey,
            query = queryMap
        ).enqueue(object : Callback<AlgoliaSearchResponse> {
            override fun onResponse(
                call: Call<AlgoliaSearchResponse>,
                response: Response<AlgoliaSearchResponse>
            ) {
                if (response.isSuccessful) {
                    val jobs = response.body()?.hits?.map { hit ->
                        mapHitToJob(hit)
                    } ?: emptyList()
                    onResult(jobs)
                } else {
                    onError(Exception("Tìm kiếm không thành công"))
                }
            }

            override fun onFailure(call: Call<AlgoliaSearchResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    private fun mapHitToJob(hit: Map<String, Any>): Job {
        return Job(
            id = hit["id"] as? String ?: "",
            recruiterId = hit["recruiterId"] as? String ?: "",
            title = hit["title"] as? String ?: "",
            description = hit["description"] as? String ?: "",
            requirements = hit["requirements"] as? String ?: "",
            salary = hit["salary"] as? String ?: "",
            location = hit["location"] as? String ?: "",
            type = hit["type"] as? String ?: "",
            companyName = hit["companyName"] as? String ?: "",
            logoUrl = hit["logoUrl"] as? String ?: "",
            createdAt = hit["createdAt"] as? Long ?: 0,
            deadline = hit["deadline"] as? Long ?: 0,

        )
    }
}