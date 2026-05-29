package com.example.movierate.data.remote

data class UserStatsResponse(
    val watchedCount: Long,
    val ratingCount: Long,
    val reviewCount: Long,
    val listCount: Long
)
