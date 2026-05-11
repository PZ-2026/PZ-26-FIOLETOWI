package com.example.movierate.data.remote

data class MovieReportRequest(
    val title: String,
    val generatedBy: String?,
    val movies: List<MovieReportItemRequest>,
)
