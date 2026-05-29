package com.example.movierate.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val DEFAULT_BASE_URL = "http://10.219.99.244:8080"
    private val baseUrl = DEFAULT_BASE_URL

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

    val reportsApi: ReportsApi by lazy {
        retrofit.create(ReportsApi::class.java)
    }

    val listsApi: ListsApi by lazy {
        retrofit.create(ListsApi::class.java)
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    val adminApi: AdminApi by lazy {
        retrofit.create(AdminApi::class.java)
    }
}
