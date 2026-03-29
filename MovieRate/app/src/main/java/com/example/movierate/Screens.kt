package com.example.movierate

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val DarkBackground = Color(0xFF151A23)
val DarkSurface = Color(0xFF1E2532)
val TextBlue = Color(0xFF3B82F6)
val GradientStart = Color(0xFF0052D4)
val GradientEnd = Color(0xFF9400D3)

val PrimaryGradientBrush = Brush.linearGradient(colors = listOf(GradientStart, GradientEnd))

@OptIn(ExperimentalTextApi::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf(false) }

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
                            if (email == "admin@test.pl" && password == "1234") {
                                errorMessage = false
                                onLoginSuccess()
                            } else {
                                errorMessage = true
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

                if (errorMessage) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Błędny e-mail lub hasło!",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onLogout: () -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = TextBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("MovieRate", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.background(DarkSurface)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Wyloguj się", color = Color.White) },
                            onClick = {
                                menuExpanded = false
                                onLogout()
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        bottomBar = {
            MovieRateBottomNav()
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item { HeroSection() }
            item { StatsSection() }
            item { TopRatedSection() }
        }
    }
}

@Composable
fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = PrimaryGradientBrush)
            .padding(24.dp)
    ) {
        Column {
            Text(
                "Odkryj, Oceń,\nZapisz",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Twoja osobista baza filmów i seriali. Organizuj obejrzane produkcje, wystawiaj oceny i dziel się opiniami.",
                color = Color.White,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3441)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Rozpocznij za darmo", color = Color.White)
                }
                OutlinedButton(
                    onClick = { },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF9400D3)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Top Rated")
                }
            }
        }
    }
}

@Composable
fun StatsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem("12,547", "Filmów")
        StatItem("45,891", "Recenzji")
        StatItem("8,234", "Użytkowników")
    }
}

@Composable
fun StatItem(number: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(number, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextBlue)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun TopRatedSection() {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Najwyżej\noceniane",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp,
                    lineHeight = 24.sp
                )
            }
            TextButton(onClick = { }) {
                Text("Zobacz wszystkie", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(mockMovies) { movie ->
                MovieCardMinimal(movie)
            }
        }
    }
}

@Composable
fun MovieCardMinimal(movie: Movie) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(200.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)) {
            Text(
                movie.title,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MovieRateBottomNav() {
    NavigationBar(
        containerColor = DarkBackground,
        contentColor = Color.Gray
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Główna") },
            label = { Text("Główna") },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TextBlue,
                selectedTextColor = TextBlue,
                indicatorColor = Color.Transparent,
                unselectedIconColor = Color.Gray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Szukaj") },
            label = { Text("Szukaj") },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Listy") },
            label = { Text("Listy") },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
            label = { Text("Profil") },
            selected = false,
            onClick = { }
        )
    }
}