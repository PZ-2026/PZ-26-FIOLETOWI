package com.example.movierate.data.remote

data class UserListItemResponse(
    val id: Long,
    val movieId: Long,
    val movieTitle: String,
    val releaseYear: Int?,
    val type: String?,
    val averageRating: Double,
    val position: Int?,
    val imageUrl: String? = null
)
