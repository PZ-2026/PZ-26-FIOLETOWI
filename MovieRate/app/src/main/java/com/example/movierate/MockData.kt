package com.example.movierate

data class Movie(
    val id: Int,
    val title: String,
    val rating: Double,
    val year: Int,
    val type: String
)

val mockMovies = listOf(
    Movie(1, "Skazani na Shawshank", 9.3, 1994, "Film"),
    Movie(2, "Ojciec Chrzestny", 9.2, 1972, "Film"),
    Movie(3, "Breaking Bad", 9.5, 2008, "Serial"),
    Movie(4, "Mroczny Rycerz", 9.0, 2008, "Film"),
    Movie(5, "Czarnobyl", 9.4, 2019, "Serial")
)