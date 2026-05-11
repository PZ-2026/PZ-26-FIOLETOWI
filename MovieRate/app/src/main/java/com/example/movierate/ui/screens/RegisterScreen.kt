package com.example.movierate.ui.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movierate.data.remote.AuthResponse
import com.example.movierate.data.remote.RegisterRequest
import com.example.movierate.data.remote.RetrofitClient
import com.example.movierate.ui.components.DarkBackground
import com.example.movierate.ui.components.DarkSurface
import com.example.movierate.ui.components.PrimaryGradientBrush
import com.example.movierate.ui.components.TextBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalTextApi::class)
@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit,
    onRegisterSuccess: (AuthResponse) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
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
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Rejestracja",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                RegisterTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        errorMessage = ""
                    },
                    label = "Nazwa użytkownika"
                )

                Spacer(modifier = Modifier.height(14.dp))

                RegisterTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = ""
                    },
                    label = "Adres e-mail",
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(14.dp))

                RegisterTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = ""
                    },
                    label = "Hasło",
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                RegisterTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = ""
                    },
                    label = "Powtórz hasło",
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(28.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(brush = PrimaryGradientBrush, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            val validationError = validateRegisterInput(
                                username = username,
                                email = email,
                                password = password,
                                confirmPassword = confirmPassword
                            )
                            if (validationError != null) {
                                errorMessage = validationError
                                return@clickable
                            }

                            coroutineScope.launch {
                                isLoading = true
                                try {
                                    val response = RetrofitClient.api.register(
                                        RegisterRequest(
                                            username = username.trim(),
                                            email = email.trim(),
                                            password = password
                                        )
                                    )
                                    if (response.isSuccessful) {
                                        val authResponse = response.body()
                                        if (authResponse != null) {
                                            errorMessage = ""
                                            onRegisterSuccess(authResponse)
                                        } else {
                                            errorMessage = "Serwer nie zwrócił danych użytkownika."
                                        }
                                    } else {
                                        val errBody = response.errorBody()?.string()
                                        errorMessage = errBody?.takeIf { it.isNotBlank() }
                                            ?: "Nie udało się utworzyć konta. Kod błędu: ${response.code()}"
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
                            text = "Zarejestruj się",
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
                    text = "Masz już konto? Zaloguj się",
                    color = TextBlue,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onBackToLogin() }
                )
            }
        }
    }
}

@Composable
private fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = authFieldColors()
    )
}

private fun validateRegisterInput(
    username: String,
    email: String,
    password: String,
    confirmPassword: String
): String? {
    val trimmedUsername = username.trim()
    val trimmedEmail = email.trim()

    return when {
        trimmedUsername.isBlank() -> "Podaj nazwę użytkownika."
        trimmedUsername.length < 3 -> "Nazwa użytkownika musi mieć co najmniej 3 znaki."
        trimmedEmail.isBlank() -> "Podaj adres e-mail."
        !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> "Podaj poprawny adres e-mail."
        password.isBlank() -> "Podaj hasło."
        password.length < 8 -> "Hasło musi mieć co najmniej 8 znaków."
        confirmPassword.isBlank() -> "Powtórz hasło."
        password != confirmPassword -> "Hasła muszą być takie same."
        else -> null
    }
}
