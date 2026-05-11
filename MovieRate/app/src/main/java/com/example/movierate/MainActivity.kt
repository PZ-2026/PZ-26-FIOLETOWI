package com.example.movierate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movierate.ui.theme.MovieRateTheme
import com.example.movierate.ui.components.MainTopAppBar
import com.example.movierate.ui.components.MovieRateBottomNav
import com.example.movierate.ui.components.DarkBackground
import com.example.movierate.ui.screens.HomeScreen
import com.example.movierate.ui.screens.LoginScreen
import com.example.movierate.ui.screens.RegisterScreen
import com.example.movierate.ui.screens.SearchScreen
import com.example.movierate.ui.screens.ListsScreen
import com.example.movierate.ui.screens.ProfileScreen
import com.example.movierate.ui.screens.AdminScreen
import com.example.movierate.data.remote.AuthResponse

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieRateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var currentUser by remember { mutableStateOf<AuthResponse?>(null) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "login"
    
    val isFullScreen = currentRoute == "login" || currentRoute == "register"

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            if (!isFullScreen) {
                MainTopAppBar(
                    onLogout = {
                        currentUser = null
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    navController = navController
                )
            }
        },
        bottomBar = {
            if (!isFullScreen) {
                MovieRateBottomNav(
                    currentRoute = currentRoute,
                    navController = navController
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController, 
            startDestination = "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("login") {
                LoginScreen(
                    onRegisterClick = {
                        navController.navigate("register")
                    },
                    onLoginSuccess = { user ->
                        currentUser = user
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("register") {
                RegisterScreen(
                    onBackToLogin = {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    onRegisterSuccess = { user ->
                        currentUser = user
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("home") {
                HomeScreen()
            }
            composable("search") {
                SearchScreen()
            }
            composable("lists") {
                ListsScreen()
            }
            composable("profile") {
                ProfileScreen(
                    user = currentUser,
                    onLogout = {
                        currentUser = null
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable("admin") {
                AdminScreen()
            }
        }
    }
}
