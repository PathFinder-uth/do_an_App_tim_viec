package com.example.pathfinder.data.remote

data class AlgoliaSearchResponse(
    val hits: List<Map<String, Any>>,
    val nbHits: Int,
    val page: Int,
    val nbPages: Int
)