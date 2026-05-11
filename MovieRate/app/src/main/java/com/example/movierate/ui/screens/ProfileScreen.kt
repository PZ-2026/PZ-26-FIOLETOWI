package com.example.movierate.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movierate.ui.components.DarkBackground
import com.example.movierate.ui.components.DarkSurface
import com.example.movierate.data.remote.AuthResponse
import com.example.movierate.data.remote.ReportDownloadManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    user: AuthResponse?,
    onLogout: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }

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
                    onSave = { isEditing = false },
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
            ProfileStatsGrid()
        }

        item {
            FavoriteGenresCard()
        }

        item {
            RecentActivityCard()
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
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(Color(0xFF2563EB), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(initial, color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
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
fun EditProfileCard(user: AuthResponse?, onSave: () -> Unit, onCancel: () -> Unit) {
    val username = user?.username ?: ""
    val email = user?.email ?: ""
    val initial = username.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

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

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(Color(0xFF2563EB), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(initial, color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { /* TODO */ }) {
                    Text("Zmień zdjęcie", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { },
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
                value = email,
                onValueChange = { },
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

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Button(
                    onClick = onSave,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Zapisz zmiany", fontWeight = FontWeight.Bold)
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
fun ProfileStatsGrid() {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatBox(
                title = "Obejrzane",
                count = "127",
                subtitle = "filmów i seriali",
                icon = Icons.AutoMirrored.Filled.List,
                iconTint = Color(0xFF3B82F6),
                modifier = Modifier.weight(1f)
            )
            StatBox(
                title = "Oceny",
                count = "89",
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
                count = "24",
                subtitle = "napisanych",
                icon = Icons.Default.Create, // Placeholder for message/chat
                iconTint = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            )
            StatBox(
                title = "Listy",
                count = "5",
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
fun FavoriteGenresCard() {
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

            val maxCount = 45f
            GenreProgressRow("Dramat", 45, maxCount)
            GenreProgressRow("Akcja", 32, maxCount)
            GenreProgressRow("Komedia", 25, maxCount)
            GenreProgressRow("Sci-Fi", 15, maxCount)
            GenreProgressRow("Horror", 10, maxCount)
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
            progress = { count / maxCount },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = Color.White,
            trackColor = Color(0xFF2A3441)
        )
    }
}

@Composable
fun RecentActivityCard() {
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

            ActivityItemRow(
                icon = Icons.Default.Star,
                iconColor = Color(0xFF3B82F6),
                iconBgCol = Color(0xFF1E3A8A),
                textNormal = "Oceniłeś film ",
                textBold = "Mroczny Rycerz",
                time = "2 dni temu"
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            ActivityItemRow(
                icon = Icons.Default.Email, // Replace chat with Email/Message type
                iconColor = Color(0xFF10B981),
                iconBgCol = Color(0xFF064E3B),
                textNormal = "Dodałeś recenzję do ",
                textBold = "Breaking Bad",
                time = "5 dni temu"
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            ActivityItemRow(
                icon = Icons.Default.Menu,
                iconColor = Color(0xFF9333EA),
                iconBgCol = Color(0xFF4C1D95),
                textNormal = "Dodałeś ",
                textBold = "Incepcja",
                textNormalSuffix = " do listy",
                time = "1 tydzień temu"
            )
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
                generatedBy = user?.username ?: user?.email ?: "MovieRate"
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

@Suppress("unused")
@Composable
fun ReportsCard() {
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
            Text("Generuj raporty swojej aktywności", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(24.dp))

            ReportButton("Moje oceny")
            Spacer(modifier = Modifier.height(12.dp))
            ReportButton("Moje recenzje")
            Spacer(modifier = Modifier.height(12.dp))
            ReportButton("Raport aktywności")
        }
    }
}

@Composable
fun ReportButton(title: String) {
    Button(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151A23)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
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
