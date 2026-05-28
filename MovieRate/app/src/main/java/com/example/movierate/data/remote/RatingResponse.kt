package com.example.movierate.data.remote

data class RatingResponse(
    val averageRating: Double = 0.0,
    val userRating: Int? = null
)
