package com.example.movierate.data.remote

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthApiContractTest {

    private lateinit var server: MockWebServer
    private lateinit var authApi: AuthApi

    @Before
    fun setUp() {
        server = MockWebServer()
        authApi = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun login_sendsRequestToBackendEndpointAndMapsResponse() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    """
                    {
                      "message": "Login successful",
                      "username": "anna",
                      "email": "anna@example.com",
                      "role": "USER"
                    }
                    """.trimIndent()
                )
                .addHeader("Content-Type", "application/json")
        )

        val response = authApi.login(LoginRequest("anna@example.com", "secret123"))
        val recordedRequest = server.takeRequest()
        val requestBody = recordedRequest.body.readUtf8()

        assertTrue(response.isSuccessful)
        assertEquals("/api/auth/login", recordedRequest.path)
        assertEquals("POST", recordedRequest.method)
        assertTrue(requestBody.contains("\"email\":\"anna@example.com\""))
        assertTrue(requestBody.contains("\"password\":\"secret123\""))
        assertEquals("Login successful", response.body()?.message)
        assertEquals("anna", response.body()?.username)
        assertEquals("anna@example.com", response.body()?.email)
        assertEquals("USER", response.body()?.role)
    }
}
