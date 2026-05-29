package com.example.movierate.data.remote

data class UpdateProfileRequest(
    val username: String,
    val email: String,
    val password: String? = null,
    val profilePictureUrl: String? = null
)
