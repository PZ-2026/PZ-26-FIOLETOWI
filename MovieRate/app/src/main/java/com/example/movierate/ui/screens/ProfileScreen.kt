package com.example.movierate.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movierate.ui.components.DarkBackground
import com.example.movierate.ui.components.DarkSurface
import com.example.movierate.ui.components.TextBlue
import com.example.movierate.data.remote.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    user: AuthResponse?,
    onLogout: () -> Unit,
    onUserUpdated: ((AuthResponse) -> Unit)? = null
) {
    var isEditing by remember { mutableStateOf(false) }
    var userStats by remember { mutableStateOf<UserStatsResponse?>(null) }
    var userGenres by remember { mutableStateOf<List<GenreStatResponse>>(emptyList()) }
    var userActivity by remember { mutableStateOf<List<ActivityResponse>>(emptyList()) }
    var isLoadingStats by remember { mutableStateOf(true) }
    val actualUserId = user?.userId ?: 0L

    LaunchedEffect(user) {
        if (user == null || actualUserId == 0L) return@LaunchedEffect
        isLoadingStats = true
        try {
            val statsResponse = RetrofitClient.userApi.getUserStats(actualUserId)
            if (statsResponse.isSuccessful) {
                userStats = statsResponse.body()
            }

            val genresResponse = RetrofitClient.userApi.getUserGenres(actualUserId)
            if (genresResponse.isSuccessful) {
                userGenres = genresResponse.body().orEmpty()
            }

            val activityResponse = RetrofitClient.userApi.getUserActivity(actualUserId)
            if (activityResponse.isSuccessful) {
                userActivity = activityResponse.body().orEmpty()
            }
        } catch (_: Exception) {
            // Silently fail - UI will show empty/placeholder data
        } finally {
            isLoadingStats = false
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            if (isEditing) {
                EditProfileCard(
                    user = user,
                    onSave = { updatedUser ->
                        if (updatedUser != null) {
                            onUserUpdated?.invoke(updatedUser)
                        }
                        isEditing = false
                    },
                    onCancel = { isEditing = false }
                )
            } else {
                ProfileHeaderCard(
                    user = user,
                    onEditClick = { isEditing = true },
                    onLogout = onLogout
                )
            }
        }

        item {
            ProfileStatsGrid(stats = userStats, isLoading = isLoadingStats)
        }

        item {
            FavoriteGenresCard(genres = userGenres, isLoading = isLoadingStats)
        }

        item {
            RecentActivityCard(activities = userActivity, isLoading = isLoadingStats)
        }

        item {
            ReportsCard(user = user)
        }
    }
}

@Composable
fun ProfileHeaderCard(user: AuthResponse?, onEditClick: () -> Unit, onLogout: () -> Unit) {
    val username = user?.username ?: "Nieznany użytkownik"
    val email = user?.email ?: "Brak adresu e-mail"
    val role = user?.role?.let { formatRole(it) } ?: "Użytkownik"
    val joinedAt = user?.createdAt?.let { formatJoinedDate(it) } ?: "Brak daty dołączenia"
    val initial = username.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    val profilePictureUrl = user?.profilePictureUrl

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!profilePictureUrl.isNullOrBlank()) {
                val imageBitmap = remember(profilePictureUrl) {
                    try {
                        val base64Str = profilePictureUrl.substringAfter("base64,")
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
                        contentDescription = "Zdjęcie profilowe",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB), shape = CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .background(Color(0xFF2563EB), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(initial, color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(Color(0xFF2563EB), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(initial, color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = username,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = role,
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(email, color = Color.Gray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Dołączył: $joinedAt", color = Color.Gray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edytuj profil", fontWeight = FontWeight.Bold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                }

                Button(
                    onClick = onLogout,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151A23), contentColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Wyloguj", maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
fun EditProfileCard(user: AuthResponse?, onSave: (AuthResponse?) -> Unit, onCancel: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var editUsername by remember { mutableStateOf(user?.username ?: "") }
    var editEmail by remember { mutableStateOf(user?.email ?: "") }
    var editPassword by remember { mutableStateOf("") }
    var editPasswordConfirm by remember { mutableStateOf("") }
    var editProfilePictureUrl by remember { mutableStateOf(user?.profilePictureUrl ?: "") }
    var isSaving by remember { mutableStateOf(false) }
    var showPasswordFields by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    val initial = (user?.username?.firstOrNull()?.uppercaseChar()?.toString()) ?: "?"

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes() ?: return@rememberLauncherForActivityResult
                inputStream.close()
                val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
                val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
                editProfilePictureUrl = "data:$mimeType;base64,$base64"
            } catch (e: Exception) {
                Toast.makeText(context, "Błąd wczytywania zdjęcia: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Edytuj profil", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = onCancel) {
                    Icon(Icons.Default.Close, contentDescription = "Zamknij", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile picture section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar preview with base64 decoding
                if (editProfilePictureUrl.isNotBlank()) {
                    val previewBitmap = remember(editProfilePictureUrl) {
                        try {
                            val base64Str = editProfilePictureUrl.substringAfter("base64,")
                            val decodedBytes = android.util.Base64.decode(base64Str, android.util.Base64.DEFAULT)
                            val bitmap = android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                            if (bitmap != null) bitmap.asImageBitmap() else null
                        } catch (e: Exception) {
                            null
                        }
                    }
                    if (previewBitmap != null) {
                        Image(
                            bitmap = previewBitmap,
                            contentDescription = "Zdjęcie profilowe",
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2563EB), shape = CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .background(Color(0xFF2563EB), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(initial, color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .background(Color(0xFF2563EB), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(initial, color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Pick image button
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3441), contentColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Wybierz zdjęcie", fontWeight = FontWeight.Bold)
                }
                if (editProfilePictureUrl.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    TextButton(onClick = { editProfilePictureUrl = "" }) {
                        Text("Usuń zdjęcie", color = Color(0xFFEF4444), fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = editUsername,
                onValueChange = { editUsername = it },
                label = { Text("Nazwa użytkownika", color = Color.White, fontWeight = FontWeight.Bold) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkBackground,
                    unfocusedBorderColor = DarkBackground,
                    focusedContainerColor = DarkBackground,
                    unfocusedContainerColor = DarkBackground,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = editEmail,
                onValueChange = { editEmail = it },
                label = { Text("Email", color = Color.White, fontWeight = FontWeight.Bold) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkBackground,
                    unfocusedBorderColor = DarkBackground,
                    focusedContainerColor = DarkBackground,
                    unfocusedContainerColor = DarkBackground,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password change toggle
            TextButton(onClick = { showPasswordFields = !showPasswordFields }) {
                Icon(
                    if (showPasswordFields) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TextBlue,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    if (showPasswordFields) "Ukryj zmianę hasła" else "Zmień hasło",
                    color = TextBlue,
                    fontSize = 14.sp
                )
            }

            if (showPasswordFields) {
                OutlinedTextField(
                    value = editPassword,
                    onValueChange = {
                        editPassword = it
                        passwordError = null
                    },
                    label = { Text("Nowe hasło", color = Color.White, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkBackground,
                        unfocusedBorderColor = DarkBackground,
                        focusedContainerColor = DarkBackground,
                        unfocusedContainerColor = DarkBackground,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = editPasswordConfirm,
                    onValueChange = {
                        editPasswordConfirm = it
                        passwordError = null
                    },
                    label = { Text("Potwierdź hasło", color = Color.White, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    isError = passwordError != null,
                    supportingText = passwordError?.let { { Text(it, color = Color(0xFFEF4444)) } },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkBackground,
                        unfocusedBorderColor = DarkBackground,
                        focusedContainerColor = DarkBackground,
                        unfocusedContainerColor = DarkBackground,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Button(
                    onClick = {
                        // Validate password
                        if (editPassword.isNotEmpty() && editPassword != editPasswordConfirm) {
                            passwordError = "Hasła nie są zgodne"
                            return@Button
                        }
                        if (editPassword.isNotEmpty() && editPassword.length < 4) {
                            passwordError = "Hasło musi mieć co najmniej 4 znaki"
                            return@Button
                        }
                        isSaving = true
                        coroutineScope.launch {
                            try {
                                val response = RetrofitClient.api.updateProfile(
                                    userId = user?.userId ?: 0L,
                                    request = UpdateProfileRequest(
                                        username = editUsername,
                                        email = editEmail,
                                        password = editPassword.ifBlank { null },
                                        profilePictureUrl = editProfilePictureUrl.ifBlank { null }
                                    )
                                )
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Profil zaktualizowany", Toast.LENGTH_SHORT).show()
                                    val updatedUser = response.body()
                                    onSave(updatedUser)
                                } else {
                                    Toast.makeText(context, "Blad: ${response.errorBody()?.string() ?: "Nieznany blad"}", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Blad polaczenia: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                isSaving = false
                            }
                        }
                    },
                    enabled = !isSaving,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.Black)
                    } else {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Zapisz zmiany", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151A23), contentColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Anuluj")
                }
            }
        }
    }
}

@Composable
fun ProfileStatsGrid(stats: UserStatsResponse?, isLoading: Boolean) {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatBox(
                title = "Obejrzane",
                count = if (isLoading) "..." else (stats?.watchedCount?.toString() ?: "0"),
                subtitle = "filmów i seriali",
                icon = Icons.AutoMirrored.Filled.List,
                iconTint = Color(0xFF3B82F6),
                modifier = Modifier.weight(1f)
            )
            StatBox(
                title = "Oceny",
                count = if (isLoading) "..." else (stats?.ratingCount?.toString() ?: "0"),
                subtitle = "wystawionych",
                icon = Icons.Default.Star,
                iconTint = Color(0xFFF59E0B),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatBox(
                title = "Recenzje",
                count = if (isLoading) "..." else (stats?.reviewCount?.toString() ?: "0"),
                subtitle = "napisanych",
                icon = Icons.Default.Create,
                iconTint = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            )
            StatBox(
                title = "Listy",
                count = if (isLoading) "..." else (stats?.listCount?.toString() ?: "0"),
                subtitle = "utworzonych",
                icon = Icons.Default.Menu,
                iconTint = Color(0xFF9333EA),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatBox(title: String, count: String, subtitle: String, icon: ImageVector, iconTint: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(150.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Column {
                Text(count, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun FavoriteGenresCard(genres: List<GenreStatResponse>, isLoading: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text("Ulubione gatunki", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Na podstawie obejrzanych filmów", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = TextBlue)
                }
            } else if (genres.isEmpty()) {
                Text("Brak danych o gatunkach", color = Color.Gray, fontSize = 14.sp)
            } else {
                val maxCount = genres.maxOfOrNull { it.count }?.toFloat() ?: 1f
                genres.forEach { genre ->
                    GenreProgressRow(genre.name, genre.count.toInt(), maxCount)
                }
            }
        }
    }
}

@Composable
fun GenreProgressRow(genre: String, count: Int, maxCount: Float) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(genre, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("$count filmów", color = Color.Gray, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { if (maxCount > 0) count / maxCount else 0f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = Color.White,
            trackColor = Color(0xFF2A3441)
        )
    }
}

@Composable
fun RecentActivityCard(activities: List<ActivityResponse>, isLoading: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text("Ostatnia aktywność", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Twoje ostatnie działania w systemie", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = TextBlue)
                }
            } else if (activities.isEmpty()) {
                Text("Brak aktywności", color = Color.Gray, fontSize = 14.sp)
            } else {
                activities.forEachIndexed { index, activity ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    val (icon, iconColor, iconBg) = when (activity.type) {
                        "rating" -> Triple(Icons.Default.Star, Color(0xFF3B82F6), Color(0xFF1E3A8A))
                        "review" -> Triple(Icons.Default.Email, Color(0xFF10B981), Color(0xFF064E3B))
                        "list_add" -> Triple(Icons.Default.Menu, Color(0xFF9333EA), Color(0xFF4C1D95))
                        else -> Triple(Icons.Default.Info, Color.Gray, Color(0xFF2A3441))
                    }

                    ActivityItemRow(
                        icon = icon,
                        iconColor = iconColor,
                        iconBgCol = iconBg,
                        textNormal = when (activity.type) {
                            "rating" -> "Oceniłeś film "
                            "review" -> "Dodałeś recenzję do "
                            "list_add" -> "Dodałeś "
                            else -> "Akcja: "
                        },
                        textBold = activity.movieTitle,
                        textNormalSuffix = if (activity.type == "list_add") " do listy" else "",
                        time = formatActivityDate(activity.date)
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityItemRow(
    icon: ImageVector,
    iconColor: Color,
    iconBgCol: Color,
    textNormal: String,
    textBold: String,
    textNormalSuffix: String = "",
    time: String
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(iconBgCol, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.LightGray)) {
                        append(textNormal)
                    }
                    withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                        append(textBold)
                    }
                    if (textNormalSuffix.isNotEmpty()) {
                        withStyle(style = SpanStyle(color = Color.LightGray)) {
                            append(textNormalSuffix)
                        }
                    }
                },
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(time, color = Color.Gray, fontSize = 13.sp)
        }
    }
}

@Composable
fun ReportsCard(user: AuthResponse?) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var generatingReportTitle by remember { mutableStateOf<String?>(null) }

    fun generateReport(title: String) {
        generatingReportTitle = title
        coroutineScope.launch {
            val result = ReportDownloadManager.generateMovieReport(
                context = context,
                title = title,
                generatedBy = user?.username ?: user?.email ?: "MovieRate",
                userId = user?.userId
            )

            result
                .onSuccess { report ->
                    Toast.makeText(context, "Zapisano raport: ${report.fileName}", Toast.LENGTH_LONG).show()
                    if (!ReportDownloadManager.openPdf(context, report)) {
                        Toast.makeText(context, "Brak aplikacji do otwarcia PDF", Toast.LENGTH_LONG).show()
                    }
                }
                .onFailure { error ->
                    Toast.makeText(context, error.message ?: "Nie udalo sie wygenerowac raportu", Toast.LENGTH_LONG).show()
                }

            generatingReportTitle = null
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text("Raporty", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Generuj raporty swojej aktywnosci", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(24.dp))

            ReportButton(
                title = "Moje oceny",
                isLoading = generatingReportTitle == "Moje oceny",
                enabled = generatingReportTitle == null,
                onClick = { generateReport("Moje oceny") }
            )
            Spacer(modifier = Modifier.height(12.dp))
            ReportButton(
                title = "Moje recenzje",
                isLoading = generatingReportTitle == "Moje recenzje",
                enabled = generatingReportTitle == null,
                onClick = { generateReport("Moje recenzje") }
            )
            Spacer(modifier = Modifier.height(12.dp))
            ReportButton(
                title = "Raport aktywnosci",
                isLoading = generatingReportTitle == "Raport aktywnosci",
                enabled = generatingReportTitle == null,
                onClick = { generateReport("Raport aktywnosci") }
            )
        }
    }
}

@Composable
fun ReportButton(
    title: String,
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151A23)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

private fun formatRole(role: String): String {
    return when (role.uppercase()) {
        "ADMIN" -> "Administrator"
        else -> "Użytkownik"
    }
}

private fun formatJoinedDate(createdAt: String): String {
    return try {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val outputFormatter = SimpleDateFormat("d MMMM yyyy", Locale.forLanguageTag("pl-PL"))
        val date = inputFormatter.parse(createdAt.substringBefore("."))
        date?.let(outputFormatter::format) ?: createdAt.substringBefore("T")
    } catch (_: Exception) {
        createdAt.substringBefore("T")
    }
}

private fun formatActivityDate(date: String): String {
    return try {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val outputFormatter = SimpleDateFormat("d MMM yyyy", Locale.forLanguageTag("pl-PL"))
        val parsed = inputFormatter.parse(date.substringBefore("."))
        parsed?.let(outputFormatter::format) ?: date.substringBefore("T")
    } catch (_: Exception) {
        date.substringBefore("T")
    }
}
