package com.example.movierate

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movierate.data.remote.AuthResponse
import com.example.movierate.data.remote.RetrofitClient
import com.example.movierate.model.Movie
import com.example.movierate.ui.components.DarkBackground
import com.example.movierate.ui.components.MainTopAppBar
import com.example.movierate.ui.components.MovieRateBottomNav
import com.example.movierate.ui.screens.AdminScreen
import com.example.movierate.ui.screens.HomeScreen
import com.example.movierate.ui.screens.ListsScreen
import com.example.movierate.ui.screens.LoginScreen
import com.example.movierate.ui.screens.MovieDetailScreen
import com.example.movierate.ui.screens.ProfileScreen
import com.example.movierate.ui.screens.RegisterScreen
import com.example.movierate.ui.screens.SearchScreen
import com.example.movierate.ui.theme.MovieRateTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize RetrofitClient with saved base URL (if any)
        RetrofitClient.init(this)
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

fun saveUserSession(context: Context, user: AuthResponse) {
    val prefs = context.getSharedPreferences("movie_rate_session", Context.MODE_PRIVATE)
    prefs.edit().apply {
        putLong("userId", user.userId ?: 0L)
        putString("username", user.username)
        putString("email", user.email)
        putString("role", user.role)
        putString("createdAt", user.createdAt)
        putString("profilePictureUrl", user.profilePictureUrl)
        putBoolean("blocked", user.blocked)
        apply()
    }
}

fun restoreUserSession(context: Context): AuthResponse? {
    val prefs = context.getSharedPreferences("movie_rate_session", Context.MODE_PRIVATE)
    val userId = prefs.getLong("userId", 0L)
    if (userId == 0L) return null
    return AuthResponse(
        userId = userId,
        message = "",
        username = prefs.getString("username", null) ?: return null,
        email = prefs.getString("email", null) ?: return null,
        role = prefs.getString("role", null) ?: "",
        createdAt = prefs.getString("createdAt", null),
        profilePictureUrl = prefs.getString("profilePictureUrl", null),
        blocked = prefs.getBoolean("blocked", false)
    )
}

fun clearUserSession(context: Context) {
    val prefs = context.getSharedPreferences("movie_rate_session", Context.MODE_PRIVATE)
    prefs.edit().clear().apply()
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()
    var currentUser by remember { mutableStateOf(restoreUserSession(context)) }
    var isGuest by remember { mutableStateOf(false) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "login"

    val isFullScreen = currentRoute == "login" || currentRoute == "register" || currentRoute == "movie_detail"
    val startDest = if (currentUser != null || isGuest) "home" else "login"

    LaunchedEffect(currentUser?.userId, isGuest) {
        if (currentUser == null || isGuest) return@LaunchedEffect

        while (currentUser != null && !isGuest) {
            val userId = currentUser?.userId
            if (userId != null) {
                try {
                    val response = RetrofitClient.api.status(userId)
                    if (response.code() == 403 || response.code() == 404) {
                        currentUser = null
                        isGuest = false
                        clearUserSession(context)
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                        break
                    } else if (response.isSuccessful) {
                        response.body()?.let { activeUser ->
                            currentUser = activeUser
                            saveUserSession(context, activeUser)
                        }
                    }
                } catch (_: Exception) {
                    // Brak sieci nie wylogowuje użytkownika, żeby nie karać za chwilowy problem z backendem.
                }
            }
            delay(5000)
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            if (!isFullScreen) {
                MainTopAppBar(
                    onLogout = {
                        currentUser = null
                        isGuest = false
                        clearUserSession(context)
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    navController = navController,
                    isLoggedIn = currentUser != null,
                    isGuest = isGuest
                )
            }
        },
        bottomBar = {
            if (!isFullScreen) {
                MovieRateBottomNav(
                    currentRoute = currentRoute,
                    navController = navController,
                    isGuest = isGuest
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController, 
            startDestination = startDest,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("login") {
                LoginScreen(
                    onRegisterClick = {
                        navController.navigate("register")
                    },
                    onLoginSuccess = { user ->
                        currentUser = user
                        isGuest = false
                        saveUserSession(context, user)
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onContinueAsGuest = {
                        currentUser = null
                        isGuest = true
                        clearUserSession(context)
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
                        isGuest = false
                        saveUserSession(context, user)
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("home") {
                HomeScreen(
                    onNavigateToSearch = {
                        navController.navigate("search")
                    },
                    onNavigateToAllTopRated = {
                        navController.navigate("search/all/top_rated")
                    },
                    onNavigateToGenre = { genre ->
                        navController.navigate("search/$genre/genre")
                    },
                    onNavigateToNewest = {
                        navController.navigate("search/all/newest")
                    },
                    onNavigateToWatchlist = {
                        navController.navigate("lists")
                    },
                    onNavigateToMovieDetail = { movie ->
                        selectedMovie = movie
                        navController.navigate("movie_detail")
                    },
                    userId = currentUser?.userId
                )
            }
            composable(
                "search/{initialGenre}/{initialFilterType}",
                arguments = listOf(
                    navArgument("initialGenre") { type = NavType.StringType },
                    navArgument("initialFilterType") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val initialGenre = backStackEntry.arguments?.getString("initialGenre") ?: "all"
                val initialFilterType = backStackEntry.arguments?.getString("initialFilterType") ?: ""
                SearchScreen(
                    userId = currentUser?.userId,
                    initialGenre = if (initialGenre == "all") null else initialGenre,
                    initialFilterType = initialFilterType.ifEmpty { null },
                    onNavigateToList = { listId ->
                        navController.navigate("lists")
                    },
                    onNavigateToMovieDetail = { movie ->
                        selectedMovie = movie
                        navController.navigate("movie_detail")
                    }
                )
            }
            composable("search") {
                SearchScreen(
                    userId = currentUser?.userId,
                    onNavigateToList = { listId ->
                        navController.navigate("lists")
                    },
                    onNavigateToMovieDetail = { movie ->
                        selectedMovie = movie
                        navController.navigate("movie_detail")
                    }
                )
            }
            composable("lists") {
                if (currentUser == null || isGuest) {
                    navController.navigate(if (isGuest) "home" else "login") {
                        popUpTo("lists") { inclusive = true }
                    }
                } else {
                    ListsScreen(
                        user = currentUser,
                        onNavigateToMovieDetail = { movie ->
                            selectedMovie = movie
                            navController.navigate("movie_detail")
                        }
                    )
                }
            }
            composable("movie_detail") {
                val movie = selectedMovie
                if (movie != null) {
                    MovieDetailScreen(
                        movie = movie,
                        userId = currentUser?.userId,
                        onBack = {
                            navController.popBackStack()
                        },
                        onShowMessage = { msg ->
                            // Snackbar handled inside MovieDetailScreen
                        }
                    )
                }
            }
            composable("profile") {
                if (currentUser == null || isGuest) {
                    navController.navigate(if (isGuest) "home" else "login") {
                        popUpTo("profile") { inclusive = true }
                    }
                } else {
                    ProfileScreen(
                        user = currentUser,
                        onLogout = {
                            currentUser = null
                            isGuest = false
                            clearUserSession(context)
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onUserUpdated = { updatedUser ->
                            if (updatedUser != null) {
                                currentUser = updatedUser
                                saveUserSession(context, updatedUser)
                            }
                        }
                    )
                }
            }
            composable("admin") {
                if (currentUser?.role == "ADMIN" && !isGuest) {
                    AdminScreen(navController = navController)
                } else {
                    navController.navigate(if (isGuest) "home" else "login") {
                        popUpTo("admin") { inclusive = true }
                    }
                }
            }
        }
    }
}
