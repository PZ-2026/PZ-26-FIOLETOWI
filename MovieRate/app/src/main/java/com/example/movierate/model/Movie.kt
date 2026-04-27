package com.example.movierate.model

import com.example.movierate.data.remote.MovieDto

data class Movie(
    val id: Long,
    val title: String,
    val description: String,
    val rating: Double,
    val year: Int,
    val type: String
)

fun MovieDto.toUiModel(): Movie = Movie(
    id = id,
    title = title,
    description = description.orEmpty(),
    rating = averageRating,
    year = releaseYear ?: 0,
    type = if (type == "SERIES") "Serial" else "Film"
)
