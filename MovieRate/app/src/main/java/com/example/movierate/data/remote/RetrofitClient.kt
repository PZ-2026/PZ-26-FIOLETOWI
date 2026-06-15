package com.example.movierate.data.remote

import android.content.Context
import android.content.SharedPreferences
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val DEFAULT_BASE_URL = "http://10.219.99.244:8080"
    private const val PREFS_NAME = "movie_rate_config"
    private const val KEY_BASE_URL = "base_url"

    private var appContext: Context? = null
    private var _baseUrl: String = DEFAULT_BASE_URL
    var currentUserId: Long? = null

    private var retrofit: Retrofit = createRetrofit()
    private var _api: AuthApi = retrofit.create(AuthApi::class.java)
    private var _moviesApi: MoviesApi = retrofit.create(MoviesApi::class.java)
    private var _reportsApi: ReportsApi = retrofit.create(ReportsApi::class.java)
    private var _listsApi: ListsApi = retrofit.create(ListsApi::class.java)
    private var _userApi: UserApi = retrofit.create(UserApi::class.java)
    private var _adminApi: AdminApi = retrofit.create(AdminApi::class.java)

    private fun createRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                currentUserId?.let {
                    requestBuilder.addHeader("X-User-Id", it.toString())
                }
                chain.proceed(requestBuilder.build())
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(_baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getPrefs(): SharedPreferences? {
        return appContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Initialize the client with the application context.
     * Should be called once at app startup (e.g., in Application.onCreate or MainActivity.onCreate).
     */
    fun init(context: Context) {
        appContext = context.applicationContext
        val savedUrl = getPrefs()?.getString(KEY_BASE_URL, null)
        if (savedUrl != null && savedUrl.isNotBlank()) {
            _baseUrl = savedUrl
            rebuildRetrofit()
        }
    }

    /**
     * Update the base URL and persist it.
     * All subsequent API calls will use the new URL.
     */
    fun updateBaseUrl(newUrl: String) {
        if (newUrl == _baseUrl) return
        _baseUrl = newUrl
        getPrefs()?.edit()?.putString(KEY_BASE_URL, newUrl)?.apply()
        rebuildRetrofit()
    }

    /**
     * Get the currently configured base URL.
     */
    fun getCurrentBaseUrl(): String = _baseUrl

    /**
     * Reset to the default base URL.
     */
    fun resetToDefaultBaseUrl() {
        if (_baseUrl == DEFAULT_BASE_URL) return
        _baseUrl = DEFAULT_BASE_URL
        getPrefs()?.edit()?.remove(KEY_BASE_URL)?.apply()
        rebuildRetrofit()
    }

    private fun rebuildRetrofit() {
        retrofit = createRetrofit()
        _api = retrofit.create(AuthApi::class.java)
        _moviesApi = retrofit.create(MoviesApi::class.java)
        _reportsApi = retrofit.create(ReportsApi::class.java)
        _listsApi = retrofit.create(ListsApi::class.java)
        _userApi = retrofit.create(UserApi::class.java)
        _adminApi = retrofit.create(AdminApi::class.java)
    }

    val api: AuthApi get() = _api
    val moviesApi: MoviesApi get() = _moviesApi
    val reportsApi: ReportsApi get() = _reportsApi
    val listsApi: ListsApi get() = _listsApi
    val userApi: UserApi get() = _userApi
    val adminApi: AdminApi get() = _adminApi
}
