package com.example.movierate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController

val DarkBackground = Color(0xFF151A23)
val DarkSurface = Color(0xFF1E2532)
val TextBlue = Color(0xFF3B82F6)
val GradientStart = Color(0xFF0052D4)
val GradientEnd = Color(0xFF9400D3)

val PrimaryGradientBrush = Brush.linearGradient(colors = listOf(GradientStart, GradientEnd))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(onLogout: () -> Unit, navController: NavController? = null) {
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
            if (menuExpanded) {
                AppMenuOverlay(
                    onDismiss = { menuExpanded = false },
                    onLogout = onLogout,
                    navController = navController
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
    )
}

@Composable
fun AppMenuOverlay(onDismiss: () -> Unit, onLogout: () -> Unit, navController: NavController?) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp),
                shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                color = Color(0xFF0A0F16) // Slightly darker than DarkBackground
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = TextBlue, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Menu", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Zamknij", tint = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Nawigacja po aplikacji MovieRate", color = Color.Gray, fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(32.dp))

                    // Menu Items
                    MenuItemText("Główna", Icons.Default.Home) { 
                        navController?.navigate("home")
                        onDismiss() 
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    MenuItemText("Moje Listy", Icons.Default.List) {
                        navController?.navigate("lists")
                        onDismiss()
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    MenuItemText("Profil", Icons.Default.Person) {
                        navController?.navigate("profile")
                        onDismiss()
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    MenuItemText("Panel Admin", Icons.Default.Lock) {
                        // Action for admin panel
                        onDismiss()
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Footer
                    Divider(color = Color(0xFF1E2532), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Motyw", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Icon(Icons.Default.Star, contentDescription = "Motyw", tint = Color.Gray, modifier = Modifier.size(18.dp)) // Using Star instead of moon
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            onDismiss()
                            onLogout()
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TextBlue, contentColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Zaloguj się", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItemText(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
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
