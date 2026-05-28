package com.example.movierate.data.remote

data class CreateListRequest(
    val userId: Long,
    val name: String,
    val type: String
)
