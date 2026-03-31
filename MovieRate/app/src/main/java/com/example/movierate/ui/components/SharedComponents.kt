package com.example.movierate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

val DarkBackground = Color(0xFF151A23)
val DarkSurface = Color(0xFF1E2532)
val TextBlue = Color(0xFF3B82F6)
val GradientStart = Color(0xFF0052D4)
val GradientEnd = Color(0xFF9400D3)

val PrimaryGradientBrush = Brush.linearGradient(colors = listOf(GradientStart, GradientEnd))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(onLogout: () -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }
    
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
}

@Composable
fun MovieRateBottomNav(currentRoute: String = "home", navController: NavController? = null) {
    NavigationBar(
        containerColor = DarkBackground,
        contentColor = Color.Gray
    ) {
        val items = listOf(
            Triple("home", "Główna", Icons.Default.Home),
            Triple("search", "Szukaj", Icons.Default.Search),
            Triple("lists", "Listy", Icons.Default.List),
            Triple("profile", "Profil", Icons.Default.Person)
        )
        items.forEach { (route, label, icon) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController?.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TextBlue,
                    selectedTextColor = TextBlue,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}
