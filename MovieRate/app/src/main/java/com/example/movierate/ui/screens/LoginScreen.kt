package com.example.movierate.ui.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movierate.data.remote.AuthResponse
import com.example.movierate.data.remote.LoginRequest
import com.example.movierate.data.remote.RetrofitClient
import com.example.movierate.ui.components.DarkBackground
import com.example.movierate.ui.components.DarkSurface
import com.example.movierate.ui.components.PrimaryGradientBrush
import com.example.movierate.ui.components.TextBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalTextApi::class)
@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoginSuccess: (AuthResponse) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
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
                horizontalAlignment = Alignment.CenterHorizontally
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
                    onValueChange = {
                        email = it
                        errorMessage = ""
                    },
                    label = { Text("Adres e-mail") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = authFieldColors()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = ""
                    },
                    label = { Text("Hasło") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = authFieldColors()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(brush = PrimaryGradientBrush, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            val validationError = validateLoginInput(email, password)
                            if (validationError != null) {
                                errorMessage = validationError
                                return@clickable
                            }

                            coroutineScope.launch {
                                isLoading = true
                                try {
                                    val response = RetrofitClient.api.login(
                                        LoginRequest(email.trim(), password)
                                    )
                                    if (response.isSuccessful) {
                                        val authResponse = response.body()
                                        if (authResponse != null) {
                                            errorMessage = ""
                                            onLoginSuccess(authResponse)
                                        } else {
                                            errorMessage = "Serwer nie zwrócił danych użytkownika."
                                        }
                                    } else {
                                        val errBody = response.errorBody()?.string()
                                        errorMessage = errBody?.takeIf { it.isNotBlank() }
                                            ?: "Nie udało się zalogować. Kod błędu: ${response.code()}"
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "Błąd sieci: ${e.message ?: "sprawdź połączenie z serwerem"}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Zaloguj się",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
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
                    modifier = Modifier.clickable { onRegisterClick() }
                )
            }
        }
    }
}

private fun validateLoginInput(email: String, password: String): String? {
    val trimmedEmail = email.trim()

    return when {
        trimmedEmail.isBlank() -> "Podaj adres e-mail."
        !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> "Podaj poprawny adres e-mail."
        password.isBlank() -> "Podaj hasło."
        else -> null
    }
}

@Composable
fun authFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = TextBlue,
    unfocusedBorderColor = Color.Gray,
    focusedLabelColor = TextBlue,
    unfocusedLabelColor = Color.Gray,
    cursorColor = TextBlue
)
