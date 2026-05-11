package com.example.movierate.data.remote

data class MovieReportItemRequest(
    val title: String?,
    val releaseYear: Int?,
    val type: String?,
    val averageRating: Double?,
)
