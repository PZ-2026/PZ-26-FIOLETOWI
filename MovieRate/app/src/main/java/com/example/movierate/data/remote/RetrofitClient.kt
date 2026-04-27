package com.example.movierate.data.remote

import com.example.movierate.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val DEFAULT_BASE_URL = "http://10.0.2.2:8080"
    private val baseUrl = BuildConfig.API_BASE_URL.ifBlank { DEFAULT_BASE_URL }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val moviesApi: MoviesApi by lazy {
        retrofit.create(MoviesApi::class.java)
    }
}
