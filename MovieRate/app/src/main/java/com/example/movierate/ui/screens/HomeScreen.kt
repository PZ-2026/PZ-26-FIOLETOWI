package com.example.movierate.ui.screens

import com.example.movierate.ui.components.MovieCardWithImage
import com.example.movierate.ui.components.MovieImage

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.DisposableEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movierate.data.remote.RetrofitClient
import com.example.movierate.model.Movie
import com.example.movierate.model.toUiModel
import com.example.movierate.ui.components.DarkSurface
import com.example.movierate.ui.components.PrimaryGradientBrush
import com.example.movierate.ui.components.TextBlue

data class MovieSection(
    val title: String,
    val icon: ImageVector,
    val iconColor: Color,
    val movies: List<Movie>,
    val isLoading: Boolean,
    val errorMessage: String?,
    val genreFilter: String? = null // null means no genre filter (e.g. newest, top-rated)
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToSearch: () -> Unit = {},
    onNavigateToAllTopRated: () -> Unit = {},
    onNavigateToGenre: (String) -> Unit = {},
    onNavigateToNewest: () -> Unit = {},
    onNavigateToWatchlist: () -> Unit = {},
    onNavigateToMovieDetail: (Movie) -> Unit = {},
    userId: Long? = null
) {
    var topRatedMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var newestMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var comedyMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var horrorMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var actionMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var watchlistMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var refreshKey by remember { mutableStateOf(0) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshKey++
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(refreshKey) {
        isLoading = true
        try {
            val topRatedResp = RetrofitClient.moviesApi.getTopRatedMovies(limit = 10)
            if (topRatedResp.isSuccessful) {
                topRatedMovies = topRatedResp.body().orEmpty().map { it.toUiModel() }
            }

            val newestResp = RetrofitClient.moviesApi.getNewestMovies(limit = 10)
            if (newestResp.isSuccessful) {
                newestMovies = newestResp.body().orEmpty().map { it.toUiModel() }
            }

            val comedyResp = RetrofitClient.moviesApi.getMoviesByGenre(genre = "Comedy", limit = 10)
            if (comedyResp.isSuccessful) {
                comedyMovies = comedyResp.body().orEmpty().map { it.toUiModel() }
            }

            val horrorResp = RetrofitClient.moviesApi.getMoviesByGenre(genre = "Horror", limit = 10)
            if (horrorResp.isSuccessful) {
                horrorMovies = horrorResp.body().orEmpty().map { it.toUiModel() }
            }

            val actionResp = RetrofitClient.moviesApi.getMoviesByGenre(genre = "Action", limit = 10)
            if (actionResp.isSuccessful) {
                actionMovies = actionResp.body().orEmpty().map { it.toUiModel() }
            }

            // Load user's WATCHLIST movies
            if (userId != null && userId != 0L) {
                val listsResp = RetrofitClient.listsApi.getUserLists(userId)
                if (listsResp.isSuccessful) {
                    val watchlist = listsResp.body().orEmpty().find { it.type == "WATCHLIST" }
                    if (watchlist != null) {
                        val itemsResp = RetrofitClient.listsApi.getListItems(watchlist.id)
                        if (itemsResp.isSuccessful) {
                            watchlistMovies = itemsResp.body().orEmpty().map { item ->
                                Movie(
                                    id = item.movieId,
                                    title = item.movieTitle,
                                    description = "",
                                    rating = item.averageRating,
                                    year = item.releaseYear ?: 0,
                                    type = if (item.type == "SERIES") "Serial" else "Film",
                                    imageUrl = "https://picsum.photos/seed/movie${item.movieId}/300/450"
                                )
                            }
                        }
                    }
                }
            }

            errorMessage = null
        } catch (e: Exception) {
            errorMessage = "Blad polaczenia: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item { HeroSection(onNavigateToSearch = onNavigateToSearch) }

        // Top Rated Section
        item {
            MovieSectionRow(
                title = "Najwyżej oceniane",
                icon = Icons.Default.Star,
                iconColor = Color(0xFFFFC107),
                movies = topRatedMovies,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onViewAll = onNavigateToAllTopRated,
                onMovieClick = onNavigateToMovieDetail
            )
        }

        // Newest Section
        item {
            MovieSectionRow(
                title = "Najnowsze",
                icon = Icons.Default.DateRange,
                iconColor = TextBlue,
                movies = newestMovies,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onViewAll = onNavigateToNewest,
                onMovieClick = onNavigateToMovieDetail
            )
        }

        // Comedy Section
        item {
            MovieSectionRow(
                title = "Komedia",
                icon = Icons.Default.PlayArrow,
                iconColor = Color(0xFF10B981),
                movies = comedyMovies,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onViewAll = { onNavigateToGenre("Comedy") },
                onMovieClick = onNavigateToMovieDetail
            )
        }

        // Horror Section
        item {
            MovieSectionRow(
                title = "Horror",
                icon = Icons.Default.Star,
                iconColor = Color(0xFFEF4444),
                movies = horrorMovies,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onViewAll = { onNavigateToGenre("Horror") },
                onMovieClick = onNavigateToMovieDetail
            )
        }

        // Action Section
        item {
            MovieSectionRow(
                title = "Akcja",
                icon = Icons.Default.DateRange,
                iconColor = Color(0xFFF59E0B),
                movies = actionMovies,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onViewAll = { onNavigateToGenre("Action") },
                onMovieClick = onNavigateToMovieDetail
            )
        }

        // User's Watchlist Section
        if (userId != null && userId != 0L) {
            item {
                MovieSectionRow(
                    title = "Do obejrzenia",
                    icon = Icons.Default.Favorite,
                    iconColor = Color(0xFFEC4899),
                    movies = watchlistMovies,
                    isLoading = isLoading,
                    errorMessage = if (watchlistMovies.isEmpty() && !isLoading) "Brak filmów na liście" else null,
                    onViewAll = onNavigateToWatchlist,
                    onMovieClick = onNavigateToMovieDetail
                )
            }
        }
    }
}

@Composable
fun HeroSection(onNavigateToSearch: () -> Unit = {}) {
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
                    onClick = onNavigateToSearch,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3441)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Rozpocznij za darmo", color = Color.White)
                }
                OutlinedButton(
                    onClick = onNavigateToSearch,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFF9400D3)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Top Rated")
                }
            }
        }
    }
}

@Composable
fun MovieSectionRow(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    movies: List<Movie>,
    isLoading: Boolean,
    errorMessage: String?,
    onViewAll: () -> Unit = {},
    onMovieClick: (Movie) -> Unit = {}
) {
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
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
            TextButton(onClick = onViewAll) {
                Text("Więcej", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 14.sp
                )
            }
            else -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(movies.take(10)) { movie ->
                        MovieCardWithImage(movie = movie, onClick = { onMovieClick(movie) })
                    }
                }
            }
        }
    }
}

