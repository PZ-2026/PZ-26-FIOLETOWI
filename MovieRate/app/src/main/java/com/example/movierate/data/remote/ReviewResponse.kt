package com.example.movierate.data.remote

data class ReviewResponse(
    val id: Long,
    val username: String,
    val userId: Long,
    val movieTitle: String,
    val content: String,
    val createdAt: String,
    val deleted: Boolean,
    val userRating: Int? = null,
    val profilePictureUrl: String? = null
)
