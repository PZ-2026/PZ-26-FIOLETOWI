package com.example.movierate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movierate.ui.components.*
import com.example.movierate.data.remote.LoginRequest
import com.example.movierate.data.remote.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalTextApi::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "MovieRate",
                    style = TextStyle(
                        brush = PrimaryGradientBrush,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Adres e-mail") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = TextBlue,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = TextBlue,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = TextBlue
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Hasło") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = TextBlue,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = TextBlue,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = TextBlue
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(brush = PrimaryGradientBrush, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            coroutineScope.launch {
                                try {
                                    val response = RetrofitClient.api.login(LoginRequest(email, password))
                                    if (response.isSuccessful) {
                                        errorMessage = ""
                                        onLoginSuccess()
                                    } else {
                                        val errBody = response.errorBody()?.string() ?: "Błąd serwera"
                                        errorMessage = "Brak autoryzacji: $errBody (Kod: ${response.code()})"
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "Błąd sieci: ${e.message}"
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Zaloguj się",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Nie masz konta? Zarejestruj się",
                    color = TextBlue,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { /* TODO: Nawigacja do rejestracji */ }
                )
            }
        }
    }
}
