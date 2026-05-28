package com.example.movierate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.movierate.data.remote.AddListItemRequest
import com.example.movierate.data.remote.CastMemberDto
import com.example.movierate.data.remote.CreateListRequest
import com.example.movierate.data.remote.RatingResponse
import com.example.movierate.data.remote.ReviewResponse
import com.example.movierate.data.remote.RetrofitClient
import com.example.movierate.data.remote.UserListResponse
import com.example.movierate.model.Movie
import com.example.movierate.ui.components.DarkBackground
import com.example.movierate.ui.components.DarkSurface
import com.example.movierate.ui.components.TextBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movie: Movie,
    userId: Long? = null,
    onBack: () -> Unit = {},
    onShowMessage: (String) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showListDialog by remember { mutableStateOf(false) }
    var userLists by remember { mutableStateOf<List<UserListResponse>>(emptyList()) }
    var isLoadingLists by remember { mutableStateOf(false) }
    var castMembers by remember { mutableStateOf<List<CastMemberDto>>(emptyList()) }
    var isLoadingCast by remember { mutableStateOf(true) }
    var ratingData by remember { mutableStateOf<RatingResponse?>(null) }
    var isLoadingRating by remember { mutableStateOf(true) }
    var isRatingSubmitting by remember { mutableStateOf(false) }
    var reviews by remember { mutableStateOf<List<ReviewResponse>>(emptyList()) }
    var isLoadingReviews by remember { mutableStateOf(true) }
    var reviewText by remember { mutableStateOf("") }
    var isAddingReview by remember { mutableStateOf(false) }
    var editingReviewId by remember { mutableStateOf<Long?>(null) }
    var editingReviewText by remember { mutableStateOf("") }

    // Fetch cast
    LaunchedEffect(movie.id) {
        isLoadingCast = true
        try {
            val response = RetrofitClient.moviesApi.getMovieCast(movie.id)
            if (response.isSuccessful) {
                castMembers = response.body().orEmpty()
            }
        } catch (_: Exception) { }
        isLoadingCast = false
    }

    // Fetch user rating
    LaunchedEffect(movie.id, userId) {
        if (userId != null && userId != 0L) {
            isLoadingRating = true
            try {
                val response = RetrofitClient.moviesApi.getUserRating(movie.id, userId)
                if (response.isSuccessful) {
                    ratingData = response.body()
                }
            } catch (_: Exception) { }
            isLoadingRating = false
        } else {
            isLoadingRating = false
        }
    }

    // Fetch reviews
    LaunchedEffect(movie.id) {
        isLoadingReviews = true
        try {
            val response = RetrofitClient.moviesApi.getMovieReviews(movie.id)
            if (response.isSuccessful) {
                reviews = response.body().orEmpty()
            }
        } catch (_: Exception) { }
        isLoadingReviews = false
    }

    // List selection dialog
    if (showListDialog && userId != null && userId != 0L) {
        AlertDialog(
            onDismissRequest = { showListDialog = false },
            title = {
                Text("Dodaj do listy", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    if (isLoadingLists) {
                        CircularProgressIndicator(color = TextBlue, modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else if (userLists.isEmpty()) {
                        Text("Brak list. Utwórz nową?", color = Color.Gray)
                    } else {
                        userLists.forEach { list ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        coroutineScope.launch {
                                            try {
                                                val itemsResp = RetrofitClient.listsApi.getListItems(list.id)
                                                val alreadyInList = itemsResp.isSuccessful &&
                                                    itemsResp.body().orEmpty().any { it.movieId == movie.id }
                                                if (alreadyInList) {
                                                    onShowMessage("Film jest już na liście \"${list.name}\"")
                                                } else {
                                                    val addResp = RetrofitClient.listsApi.addMovieToList(
                                                        list.id,
                                                        AddListItemRequest(movie.id, 0)
                                                    )
                                                    if (addResp.isSuccessful) {
                                                        onShowMessage("Dodano do \"${list.name}\"")
                                                    } else {
                                                        onShowMessage("Błąd dodawania do listy")
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                onShowMessage("Błąd: ${e.message}")
                                            }
                                        }
                                        showListDialog = false
                                    },
                                color = DarkSurface,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.List, contentDescription = null, tint = TextBlue, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(list.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("${list.itemCount} filmów", color = Color.Gray, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (userId != null && userId != 0L) {
                        coroutineScope.launch {
                            try {
                                val createResp = RetrofitClient.listsApi.createList(
                                    CreateListRequest(userId = userId, name = "Nowa lista", type = "CUSTOM")
                                )
                                if (createResp.isSuccessful) {
                                    val refreshResp = RetrofitClient.listsApi.getUserLists(userId)
                                    if (refreshResp.isSuccessful) {
                                        userLists = refreshResp.body().orEmpty()
                                    }
                                    onShowMessage("Utworzono nową listę")
                                }
                            } catch (e: Exception) {
                                onShowMessage("Błąd: ${e.message}")
                            }
                        }
                    }
                }) {
                    Text("+ Nowa lista", color = TextBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showListDialog = false }) {
                    Text("Anuluj", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF151A23)
        )
    }

    Scaffold(
        containerColor = DarkBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Image header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                if (movie.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = movie.imageUrl,
                        contentDescription = movie.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF0D1017)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = movie.title.first().toString(),
                            color = Color.DarkGray,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Gradient overlay at bottom
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    DarkBackground
                                )
                            )
                        )
                )

                // Back button
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .statusBarsPadding()
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }

            // Movie info
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // Title
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Rating section - average + user rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Average rating
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = String.format("%.1f", ratingData?.averageRating ?: movie.rating),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = " ogólna",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                    // User rating (if logged in)
                    if (userId != null && userId != 0L) {
                        Spacer(modifier = Modifier.width(20.dp))
                        if (!isLoadingRating && !isRatingSubmitting) {
                            val userRating = ratingData?.userRating
                            if (userRating != null) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFF3B82F6),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$userRating",
                                    color = TextBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = " twoja",
                                    color = Color.Gray,
                                    fontSize = 13.sp
                                )
                            } else {
                                Text(
                                    text = "Nie oceniono",
                                    color = Color.Gray,
                                    fontSize = 13.sp
                                )
                            }
                        } else {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = TextBlue,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFF2A3441),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = movie.year.toString(),
                            color = Color.LightGray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = Color(0xFF2A3441),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = movie.type,
                            color = Color.LightGray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Star rating picker (1-10)
                if (userId != null && userId != 0L) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Twoja ocena:",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (i in 1..10) {
                            val currentUserRating = ratingData?.userRating
                            val isFilled = (currentUserRating ?: 0) >= i
                            val isCurrentlySelected = currentUserRating == i
                            IconButton(
                                onClick = {
                                    if (!isRatingSubmitting) {
                                        isRatingSubmitting = true
                                        coroutineScope.launch {
                                            try {
                                                if (isCurrentlySelected) {
                                                    // Clicking the same star again -> remove rating
                                                    val resp = RetrofitClient.moviesApi.deleteRating(
                                                        movie.id, userId
                                                    )
                                                    if (resp.isSuccessful) {
                                                        ratingData = resp.body()
                                                        snackbarHostState.showSnackbar("Ocena usunięta")
                                                    }
                                                } else {
                                                    val resp = RetrofitClient.moviesApi.rateMovie(
                                                        movie.id,
                                                        mapOf("userId" to userId, "rating" to i)
                                                    )
                                                    if (resp.isSuccessful) {
                                                        ratingData = resp.body()
                                                        snackbarHostState.showSnackbar("Oceniono na $i/10")
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                snackbarHostState.showSnackbar("Błąd: ${e.message}")
                                            }
                                            isRatingSubmitting = false
                                        }
                                    }
                                },
                                modifier = Modifier.size(32.dp),
                                enabled = !isRatingSubmitting
                            ) {
                                Icon(
                                    imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.Star,
                                    contentDescription = "$i",
                                    tint = if (isFilled) Color(0xFFFFC107) else Color(0xFF4A5568),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                // Genres
                if (movie.genres.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        movie.genres.take(4).forEach { genre ->
                            Surface(
                                color = TextBlue.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = genre,
                                    color = TextBlue,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Cast section
                if (!isLoadingCast && castMembers.isNotEmpty()) {
                    Text(
                        text = "Obsada",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Group by role
                    val directors = castMembers.filter { it.role == "DIRECTOR" }
                    val writers = castMembers.filter { it.role == "WRITER" }
                    val actors = castMembers.filter { it.role == "ACTOR" }

                    if (directors.isNotEmpty()) {
                        CastRoleSection(label = "Reżyseria", members = directors)
                    }
                    if (writers.isNotEmpty()) {
                        CastRoleSection(label = "Scenariusz", members = writers)
                    }
                    if (actors.isNotEmpty()) {
                        CastRoleSection(label = "Aktorzy", members = actors)
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Description
                if (movie.description.isNotBlank()) {
                    Text(
                        text = "Opis",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie.description,
                        color = Color.LightGray,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Add to list button
                    Button(
                        onClick = {
                            if (userId != null && userId != 0L) {
                                coroutineScope.launch {
                                    isLoadingLists = true
                                    try {
                                        val listsResp = RetrofitClient.listsApi.getUserLists(userId)
                                        if (listsResp.isSuccessful) {
                                            userLists = listsResp.body().orEmpty()
                                            // Ensure WATCHLIST exists
                                            var watchlist = userLists.find { it.type == "WATCHLIST" }
                                            if (watchlist == null) {
                                                val createResp = RetrofitClient.listsApi.createList(
                                                    CreateListRequest(userId = userId, name = "Do obejrzenia", type = "WATCHLIST")
                                                )
                                                if (createResp.isSuccessful) {
                                                    val refreshResp = RetrofitClient.listsApi.getUserLists(userId)
                                                    if (refreshResp.isSuccessful) {
                                                        userLists = refreshResp.body().orEmpty()
                                                    }
                                                }
                                            }
                                            // Ensure FAVORITES exists
                                            var favorites = userLists.find { it.type == "FAVORITES" }
                                            if (favorites == null) {
                                                val createResp = RetrofitClient.listsApi.createList(
                                                    CreateListRequest(userId = userId, name = "Ulubione", type = "FAVORITES")
                                                )
                                                if (createResp.isSuccessful) {
                                                    val refreshResp = RetrofitClient.listsApi.getUserLists(userId)
                                                    if (refreshResp.isSuccessful) {
                                                        userLists = refreshResp.body().orEmpty()
                                                    }
                                                }
                                            }
                                        }
                                    } catch (e: Exception) {
                                        onShowMessage("Błąd: ${e.message}")
                                    } finally {
                                        isLoadingLists = false
                                    }
                                    showListDialog = true
                                }
                            } else {
                                onShowMessage("Zaloguj się, aby dodać do listy")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TextBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Dodaj do listy", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    // Favorites button
                    Button(
                        onClick = {
                            if (userId != null && userId != 0L) {
                                coroutineScope.launch {
                                    try {
                                        val listsResp = RetrofitClient.listsApi.getUserLists(userId)
                                        if (listsResp.isSuccessful) {
                                            val lists = listsResp.body().orEmpty()
                                            var favorites = lists.find { it.type == "FAVORITES" }
                                            if (favorites == null) {
                                                val createResp = RetrofitClient.listsApi.createList(
                                                    CreateListRequest(userId = userId, name = "Ulubione", type = "FAVORITES")
                                                )
                                                if (createResp.isSuccessful) {
                                                    val refreshResp = RetrofitClient.listsApi.getUserLists(userId)
                                                    if (refreshResp.isSuccessful) {
                                                        favorites = refreshResp.body().orEmpty().find { it.type == "FAVORITES" }
                                                    }
                                                }
                                            }
                                            if (favorites != null) {
                                                val itemsResp = RetrofitClient.listsApi.getListItems(favorites.id)
                                                val alreadyInFav = itemsResp.isSuccessful &&
                                                    itemsResp.body().orEmpty().any { it.movieId == movie.id }
                                                if (alreadyInFav) {
                                                    onShowMessage("Film jest już w ulubionych")
                                                } else {
                                                    val addResp = RetrofitClient.listsApi.addMovieToList(
                                                        favorites.id,
                                                        AddListItemRequest(movie.id, 0)
                                                    )
                                                    if (addResp.isSuccessful) {
                                                        onShowMessage("Dodano do ulubionych")
                                                    } else {
                                                        onShowMessage("Błąd dodawania do ulubionych")
                                                    }
                                                }
                                            } else {
                                                onShowMessage("Nie udało się utworzyć listy ulubionych")
                                            }
                                        }
                                    } catch (e: Exception) {
                                        onShowMessage("Błąd: ${e.message}")
                                    }
                                }
                            } else {
                                onShowMessage("Zaloguj się, aby dodać do ulubionych")
                            }
                        },
                        modifier = Modifier
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3441)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                    }
                }

                // Reviews section
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Recenzje",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Add / Edit review form (if logged in)
                if (userId != null && userId != 0L) {
                    val isEditing = editingReviewId != null
                    val currentText = if (isEditing) editingReviewText else reviewText
                    val onTextChange: (String) -> Unit = { value -> if (isEditing) editingReviewText = value else reviewText = value }

                    OutlinedTextField(
                        value = currentText,
                        onValueChange = onTextChange,
                        placeholder = { Text(if (isEditing) "Edytuj recenzję..." else "Napisz recenzję...", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TextBlue,
                            unfocusedBorderColor = Color(0xFF4A5568),
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                if (currentText.isNotBlank() && !isAddingReview) {
                                    isAddingReview = true
                                    coroutineScope.launch {
                                        try {
                                            val resp = if (isEditing) {
                                                RetrofitClient.moviesApi.updateReview(
                                                    movie.id, editingReviewId!!,
                                                    mapOf("userId" to userId, "content" to currentText)
                                                )
                                            } else {
                                                RetrofitClient.moviesApi.addReview(
                                                    movie.id,
                                                    mapOf("userId" to userId, "content" to currentText)
                                                )
                                            }
                                            if (resp.isSuccessful) {
                                                if (isEditing) {
                                                    editingReviewId = null
                                                    editingReviewText = ""
                                                } else {
                                                    reviewText = ""
                                                }
                                                // Refresh reviews
                                                val refreshResp = RetrofitClient.moviesApi.getMovieReviews(movie.id)
                                                if (refreshResp.isSuccessful) {
                                                    reviews = refreshResp.body().orEmpty()
                                                }
                                                snackbarHostState.showSnackbar(if (isEditing) "Zaktualizowano recenzję" else "Dodano recenzję")
                                            } else {
                                                snackbarHostState.showSnackbar(if (isEditing) "Błąd edycji recenzji" else "Błąd dodawania recenzji")
                                            }
                                        } catch (e: Exception) {
                                            snackbarHostState.showSnackbar("Błąd: ${e.message}")
                                        }
                                        isAddingReview = false
                                    }
                                }
                            },
                            enabled = currentText.isNotBlank() && !isAddingReview,
                            colors = ButtonDefaults.buttonColors(containerColor = TextBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (isEditing) "Zapisz" else "Dodaj recenzję", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        if (isEditing) {
                            OutlinedButton(
                                onClick = {
                                    editingReviewId = null
                                    editingReviewText = ""
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Anuluj", color = Color.Gray)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Reviews list
                if (isLoadingReviews) {
                    Box(modifier = Modifier.fillMaxWidth().height(60.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = TextBlue, modifier = Modifier.size(24.dp))
                    }
                } else if (reviews.isEmpty()) {
                    Text(
                        text = "Brak recenzji. Bądź pierwszy!",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                } else {
                    reviews.forEach { review ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Profile picture or fallback icon
                                        if (!review.profilePictureUrl.isNullOrBlank()) {
                                            val imageBitmap = remember(review.profilePictureUrl) {
                                                try {
                                                    val base64Str = review.profilePictureUrl.substringAfter("base64,")
                                                    val decodedBytes = android.util.Base64.decode(base64Str, android.util.Base64.DEFAULT)
                                                    val bitmap = android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                                                    if (bitmap != null) bitmap.asImageBitmap() else null
                                                } catch (e: Exception) {
                                                    null
                                                }
                                            }
                                            if (imageBitmap != null) {
                                                Image(
                                                    bitmap = imageBitmap,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .clip(CircleShape),
                                                    contentScale = ContentScale.Crop
                                                )
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .background(Color(0xFF334155), shape = CircleShape),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        Icons.Default.Person,
                                                        contentDescription = null,
                                                        tint = TextBlue,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(Color(0xFF334155), shape = CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.Person,
                                                    contentDescription = null,
                                                    tint = TextBlue,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = review.username,
                                            color = TextBlue,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Show user's rating in review
                                        if (review.userRating != null) {
                                            Text(
                                                text = "${review.userRating}/10",
                                                color = Color(0xFFFFC107),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color(0xFFFFC107),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                        }
                                        Text(
                                            text = review.createdAt.take(10),
                                            color = Color.Gray,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = review.content,
                                    color = Color.LightGray,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                                // Edit/Delete buttons for own reviews
                                if (userId != null && review.userId == userId) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        OutlinedButton(
                                            onClick = {
                                                editingReviewId = review.id
                                                editingReviewText = review.content
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextBlue)
                                        ) {
                                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Edytuj", fontSize = 12.sp)
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                coroutineScope.launch {
                                                    try {
                                                        val resp = RetrofitClient.moviesApi.deleteReview(
                                                            movie.id, review.id, userId
                                                        )
                                                        if (resp.isSuccessful) {
                                                            val refreshResp = RetrofitClient.moviesApi.getMovieReviews(movie.id)
                                                            if (refreshResp.isSuccessful) {
                                                                reviews = refreshResp.body().orEmpty()
                                                            }
                                                            snackbarHostState.showSnackbar("Usunięto recenzję")
                                                        } else {
                                                            snackbarHostState.showSnackbar("Błąd usuwania recenzji")
                                                        }
                                                    } catch (e: Exception) {
                                                        snackbarHostState.showSnackbar("Błąd: ${e.message}")
                                                    }
                                                }
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Usuń", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Show snackbar messages
    LaunchedEffect(Unit) {
        // This is a workaround - we use the onShowMessage callback
        // The actual snackbar showing is handled via the callback
    }
}

@Composable
private fun CastRoleSection(label: String, members: List<CastMemberDto>) {
    Text(
        text = label,
        color = Color.Gray,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 6.dp)
    )
    members.forEach { member ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = TextBlue.copy(alpha = 0.7f),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = member.name,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
