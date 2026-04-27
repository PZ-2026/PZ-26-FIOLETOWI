package com.example.movierate.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movierate.data.remote.RetrofitClient
import com.example.movierate.model.Movie
import com.example.movierate.model.toUiModel
import com.example.movierate.ui.components.DarkBackground
import com.example.movierate.ui.components.DarkSurface
import com.example.movierate.ui.components.TextBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var selectedType by remember { mutableStateOf("Wszystkie typy") }
    var selectedYear by remember { mutableStateOf("Wszystkie lata") }

    val types = listOf("Wszystkie typy", "Film", "Serial")
    val years = listOf("Wszystkie lata", "2023", "2022", "2021", "2019", "2017", "2016", "2014", "2013", "2011", "2010", "2008", "2005", "2001", "1999", "1994", "1972")

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.moviesApi.getMovies()
            if (response.isSuccessful) {
                movies = response.body().orEmpty().map { it.toUiModel() }
                errorMessage = null
            } else {
                errorMessage = "Nie udalo sie pobrac listy. Kod: ${response.code()}"
            }
        } catch (e: Exception) {
            errorMessage = "Blad polaczenia z backendem: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    val filteredMovies = movies.filter { movie ->
        (searchQuery.isBlank() || movie.title.contains(searchQuery, ignoreCase = true)) &&
            (selectedType == "Wszystkie typy" || movie.type == selectedType) &&
            (selectedYear == "Wszystkie lata" || movie.year.toString() == selectedYear)
    }

    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Szukaj filmow i seriali",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Wpisz tytul filmu lub serialu", color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkSurface,
                        unfocusedBorderColor = DarkSurface,
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = TextBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = { showFilters = !showFilters },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showFilters) Color.White else DarkBackground,
                        contentColor = if (showFilters) Color.Black else Color.White
                    ),
                    border = BorderStroke(1.dp, if (showFilters) Color.White else DarkSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(Icons.Default.List, contentDescription = "Filtry")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Filtry", fontWeight = FontWeight.Bold)
                }
            }

            if (showFilters) {
                Spacer(modifier = Modifier.height(24.dp))
                CustomDropdownMenu(
                    label = "Typ",
                    options = types,
                    selectedOption = selectedType,
                    onOptionSelected = { selectedType = it }
                )
                CustomDropdownMenu(
                    label = "Rok",
                    options = years,
                    selectedOption = selectedYear,
                    onOptionSelected = { selectedYear = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = "Znaleziono", color = Color.Gray, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${filteredMovies.size}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "wynikow", color = Color.Gray, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = TextBlue)
                }
            }
            errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredMovies) { movie ->
                        SearchMovieCard(movie = movie)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkSurface,
                    unfocusedBorderColor = DarkSurface,
                    focusedContainerColor = DarkBackground,
                    unfocusedContainerColor = DarkBackground,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color(0xFF151A23))
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(selectionOption, color = Color.White)
                                if (selectionOption == selectedOption) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.Gray)
                                }
                            }
                        },
                        onClick = {
                            onOptionSelected(selectionOption)
                            expanded = false
                        },
                        modifier = Modifier.background(if (selectionOption == selectedOption) Color(0xFF2A3441) else Color.Transparent)
                    )
                }
            }
        }
    }
}

@Composable
fun SearchMovieCard(movie: Movie) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFF0D1017))
            ) {
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.height(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", movie.rating),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = Color(0xFF151A23),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = movie.year.toString(),
                            color = Color.LightGray,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Surface(
                        color = Color(0xFF151A23),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = movie.type,
                            color = Color.LightGray,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2532)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.height(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Lista", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .width(36.dp)
                            .height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2532)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White, modifier = Modifier.height(18.dp))
                    }
                }
            }
        }
    }
}
