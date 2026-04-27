package com.example.movierate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movierate.data.remote.RetrofitClient
import com.example.movierate.model.Movie
import com.example.movierate.model.toUiModel
import com.example.movierate.ui.components.DarkSurface
import com.example.movierate.ui.components.PrimaryGradientBrush
import com.example.movierate.ui.components.TextBlue

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var topRatedMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.moviesApi.getTopRatedMovies(limit = 10)
            if (response.isSuccessful) {
                topRatedMovies = response.body().orEmpty().map { it.toUiModel() }
                errorMessage = null
            } else {
                errorMessage = "Nie udalo sie pobrac filmow. Kod: ${response.code()}"
            }
        } catch (e: Exception) {
            errorMessage = "Blad polaczenia z backendem: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item { HeroSection() }
        item { StatsSection(topRatedMovies.size) }
        item { TopRatedSection(topRatedMovies, isLoading, errorMessage) }
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
                "Odkryj, Ocen,\nZapisz",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Twoja osobista baza filmow i seriali. Organizuj obejrzane produkcje, wystawiaj oceny i dziel sie opiniami.",
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
fun StatsSection(movieCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(movieCount.toString(), "Top tytulow")
        StatItem("46+", "W bazie")
        StatItem("Live", "Backend")
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
fun TopRatedSection(movies: List<Movie>, isLoading: Boolean, errorMessage: String?) {
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
                    modifier = Modifier.height(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Najwyzej\noceniane",
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

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = TextBlue)
                }
            }
            errorMessage != null -> {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            else -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(movies) { movie ->
                        MovieCardMinimal(movie)
                    }
                }
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            Text(
                text = "${movie.title}\n${movie.year}",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
