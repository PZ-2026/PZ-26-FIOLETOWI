package com.example.movierate.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movierate.model.Movie
import com.example.movierate.ui.components.DarkBackground
import com.example.movierate.ui.components.DarkSurface

private val previewListMovies = listOf(
    Movie(1, "Inception", "Dreams within dreams", 9.0, 2010, "Film"),
    Movie(2, "Breaking Bad", "Drug empire story", 9.5, 2008, "Serial"),
    Movie(3, "Interstellar", "Space exploration", 8.9, 2014, "Film"),
    Movie(4, "The Dark Knight", "Batman vs Joker", 9.2, 2008, "Film")
)

enum class ListCategory(val title: String, val subtitle: String, val color: Color, val icon: ImageVector) {
    TO_WATCH("Do obejrzenia", "Filmy i seriale, które planujesz obejrzeć", Color(0xFF3B82F6), Icons.Default.DateRange),
    WATCHED("Obejrzane", "Wszystkie filmy i seriale, które już obejrzałeś", Color(0xFF10B981), Icons.Default.Check),
    FAVORITES("Ulubione", "Twoje ulubione filmy i seriale", Color(0xFFEF4444), Icons.Default.FavoriteBorder),
    TOP_10("Top 10", "Ranking Twoich 10 ulubionych produkcji", Color(0xFFF59E0B), Icons.Default.Star)
}

@Composable
fun ListsScreen(modifier: Modifier = Modifier) {
    var selectedCategory by remember { mutableStateOf(ListCategory.TO_WATCH) }
    
    Column(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Moje Listy", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            CategoryCard(
                category = ListCategory.TO_WATCH, 
                count = 3, 
                isSelected = selectedCategory == ListCategory.TO_WATCH, 
                onClick = { selectedCategory = ListCategory.TO_WATCH }, 
                modifier = Modifier.weight(1f)
            )
            CategoryCard(
                category = ListCategory.WATCHED, 
                count = 8, 
                isSelected = selectedCategory == ListCategory.WATCHED, 
                onClick = { selectedCategory = ListCategory.WATCHED }, 
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            CategoryCard(
                category = ListCategory.FAVORITES, 
                count = 4, 
                isSelected = selectedCategory == ListCategory.FAVORITES, 
                onClick = { selectedCategory = ListCategory.FAVORITES }, 
                modifier = Modifier.weight(1f)
            )
            CategoryCard(
                category = ListCategory.TOP_10, 
                count = 10, 
                isSelected = selectedCategory == ListCategory.TOP_10, 
                onClick = { selectedCategory = ListCategory.TOP_10 }, 
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Surface(
            color = DarkSurface,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            Column(modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 24.dp)) {
                    Icon(selectedCategory.icon, contentDescription = null, tint = selectedCategory.color, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(selectedCategory.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(selectedCategory.subtitle, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 24.dp))
                
                Spacer(modifier = Modifier.height(24.dp))
                
                if (selectedCategory == ListCategory.TOP_10) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        itemsIndexed(previewListMovies) { index, movie ->
                            Top10Card(movie, index + 1, selectedCategory.color)
                        }
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        items(previewListMovies) { movie ->
                            MovieCardList(movie)
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CategoryCard(
    category: ListCategory,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(130.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) BorderStroke(1.5.dp, category.color) else null
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(category.icon, contentDescription = null, tint = category.color, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(category.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Text(count.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
    }
}

@Composable
fun MovieCardList(movie: Movie) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(240.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D1017))
        ) {
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
fun Top10Card(movie: Movie, rank: Int, rankColor: Color) {
    Card(
        modifier = Modifier.width(280.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A3441)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color.Black)
            ) {
                Text(
                    "Brak obrazka",
                    color = Color.DarkGray,
                    modifier = Modifier.align(Alignment.Center)
                )
                
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 16.dp, y = 20.dp)
                        .size(48.dp)
                        .background(rankColor, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = rank.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
            
            Column(modifier = Modifier.padding(start = 20.dp, top = 32.dp, end = 20.dp, bottom = 20.dp)) {
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${movie.year} • ${movie.type}",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}
