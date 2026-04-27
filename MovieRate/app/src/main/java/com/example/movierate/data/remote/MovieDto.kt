package com.example.movierate.data.remote

data class MovieDto(
    val id: Long,
    val title: String,
    val description: String?,
    val releaseYear: Int?,
    val type: String,
    val averageRating: Double
)
