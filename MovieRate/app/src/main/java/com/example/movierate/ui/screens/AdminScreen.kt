package com.example.movierate.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movierate.ui.components.DarkBackground
import com.example.movierate.ui.components.DarkSurface
import com.example.movierate.ui.components.TextBlue
import com.example.movierate.data.remote.AdminUserResponse
import com.example.movierate.data.remote.CreateMovieRequest
import com.example.movierate.data.remote.ReportDownloadManager
import com.example.movierate.data.remote.ReviewResponse
import com.example.movierate.data.remote.RetrofitClient
import kotlinx.coroutines.launch

enum class AdminCategory(
    val title: String, 
    val color: Color, 
    val icon: ImageVector,
    val subtitle: String
) {
    USERS("Użytkownicy", Color(0xFF3B82F6), Icons.Default.Person, "Zarządzaj kontami użytkowników"),
    MOVIES("Filmy", Color(0xFF10B981), Icons.Default.PlayArrow, "Dodawaj, edytuj i usuwaj filmy"),
    REVIEWS("Recenzje", Color(0xFF9333EA), Icons.Default.Email, "Moderuj recenzje użytkowników"),
    SYSTEM("System", Color(0xFFF59E0B), Icons.Default.Settings, "Generuj raporty PDF")
}

@Composable
fun AdminScreen(modifier: Modifier = Modifier) {
    var selectedCategory by remember { mutableStateOf(AdminCategory.MOVIES) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = TextBlue, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Panel Administratora", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AdminStatCard(
                    category = AdminCategory.USERS,
                    isSelected = selectedCategory == AdminCategory.USERS,
                    onClick = { selectedCategory = AdminCategory.USERS },
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    category = AdminCategory.MOVIES,
                    isSelected = selectedCategory == AdminCategory.MOVIES,
                    onClick = { selectedCategory = AdminCategory.MOVIES },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AdminStatCard(
                    category = AdminCategory.REVIEWS,
                    isSelected = selectedCategory == AdminCategory.REVIEWS,
                    onClick = { selectedCategory = AdminCategory.REVIEWS },
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    category = AdminCategory.SYSTEM,
                    isSelected = selectedCategory == AdminCategory.SYSTEM,
                    onClick = { selectedCategory = AdminCategory.SYSTEM },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            Surface(
                color = DarkSurface,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    when (selectedCategory) {
                        AdminCategory.MOVIES -> AdminMoviesContent()
                        AdminCategory.USERS -> AdminUsersContent()
                        AdminCategory.REVIEWS -> AdminReviewsContent()
                        AdminCategory.SYSTEM -> AdminReportsContent()
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(
    category: AdminCategory,
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
                Icon(category.icon, contentDescription = null, tint = category.color, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(category.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Column {
                Text(category.subtitle, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMoviesContent() {
    var title by remember { mutableStateOf("") }
    var releaseYear by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Film") }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Text("Zarządzanie filmami", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(4.dp))
    Text("Dodaj, edytuj lub usuń filmy z bazy danych", color = Color.Gray, fontSize = 14.sp)
    
    Spacer(modifier = Modifier.height(24.dp))
    
    AdminTextField("Tytuł", "Wprowadź tytuł filmu", value = title, onValueChange = { title = it })
    Spacer(modifier = Modifier.height(16.dp))
    AdminTextField("Rok produkcji", "2024", value = releaseYear, onValueChange = { releaseYear = it })
    Spacer(modifier = Modifier.height(16.dp))
    AdminTextField("Opis", "Wprowadź opis filmu", minLines = 3, value = description, onValueChange = { description = it })
    Spacer(modifier = Modifier.height(16.dp))
    
    // Type Dropdown
    Text("Typ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    Spacer(modifier = Modifier.height(8.dp))
    var typeExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = typeExpanded,
        onExpandedChange = { typeExpanded = it }
    ) {
        OutlinedTextField(
            value = selectedType,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DarkBackground,
                unfocusedBorderColor = DarkBackground,
                focusedContainerColor = DarkBackground,
                unfocusedContainerColor = DarkBackground,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = typeExpanded,
            onDismissRequest = { typeExpanded = false },
            modifier = Modifier.background(Color(0xFF151A23))
        ) {
            listOf("Film", "Serial").forEach { type ->
                DropdownMenuItem(
                    text = { Text(type, color = Color.White) },
                    onClick = {
                        selectedType = type
                        typeExpanded = false
                    },
                    modifier = Modifier.background(if (type == selectedType) Color(0xFF2A3441) else Color.Transparent)
                )
            }
        }
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    Text("Gatunki", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("Dramat", "Kryminał", "Akcja", "Sci-Fi", "Thriller").forEach { genre ->
            Surface(
                color = DarkBackground,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = genre, 
                    color = Color.LightGray, 
                    fontSize = 12.sp, 
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
    
    if (errorMessage != null) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(errorMessage ?: "", color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
    }
    if (successMessage != null) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(successMessage ?: "", color = Color(0xFF10B981), fontSize = 13.sp)
    }
    
    Spacer(modifier = Modifier.height(32.dp))
    
    Button(
        onClick = {
            if (title.isBlank() || releaseYear.isBlank()) {
                errorMessage = "Tytuł i rok produkcji są wymagane"
                return@Button
            }
            errorMessage = null
            successMessage = null
            isSaving = true
            coroutineScope.launch {
                try {
                    val request = CreateMovieRequest(
                        title = title.trim(),
                        description = description.trim(),
                        releaseYear = releaseYear.trim().toIntOrNull() ?: 0,
                        type = selectedType
                    )
                    val response = RetrofitClient.adminApi.createMovie(request)
                    if (response.isSuccessful) {
                        successMessage = "Film \"${title}\" został dodany!"
                        title = ""
                        releaseYear = ""
                        description = ""
                        selectedType = "Film"
                    } else {
                        errorMessage = "Błąd: ${response.code()} ${response.message()}"
                    }
                } catch (e: Exception) {
                    errorMessage = "Błąd połączenia: ${e.message}"
                } finally {
                    isSaving = false
                }
            }
        },
        enabled = !isSaving,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        if (isSaving) {
            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.Black)
        } else {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text("Dodaj film", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AdminTextField(label: String, placeholder: String, minLines: Int = 1, value: String = placeholder, onValueChange: (String) -> Unit = {}) {
    Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = if (value == placeholder && minLines == 1) "" else value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        minLines = minLines,
        placeholder = { Text(placeholder, color = Color.Gray) },
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

@Composable
fun AdminUsersContent() {
    var users by remember { mutableStateOf<List<AdminUserResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.adminApi.getUsers()
            if (response.isSuccessful) {
                users = response.body().orEmpty()
            } else {
                errorMessage = "Błąd ładowania: ${response.code()}"
            }
        } catch (e: Exception) {
            errorMessage = "Błąd połączenia: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Text("Zarządzanie użytkownikami", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(4.dp))
    Text("Przeglądaj i zarządzaj kontami użytkowników", color = Color.Gray, fontSize = 14.sp)
    
    Spacer(modifier = Modifier.height(24.dp))
    
    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TextBlue)
            }
        }
        errorMessage != null -> {
            Text(errorMessage ?: "", color = MaterialTheme.colorScheme.error)
        }
        users.isEmpty() -> {
            Text("Brak użytkowników", color = Color.Gray)
        }
        else -> {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("ID", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.1f))
                Text("Nazwa", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.25f))
                Text("Email", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.3f))
                Text("Rola", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.15f))
                Text("Akcje", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.2f))
            }
            Divider(color = Color(0xFF2A3441))
            
            users.forEach { user ->
                AdminUserRow(
                    user = user,
                    onToggleRole = {
                        coroutineScope.launch {
                            try {
                                val newRole = if (user.role == "ADMIN") "USER" else "ADMIN"
                                val resp = RetrofitClient.adminApi.updateUserRole(user.id, mapOf("role" to newRole))
                                if (resp.isSuccessful) {
                                    users = users.map { if (it.id == user.id) it.copy(role = newRole) else it }
                                    Toast.makeText(context, "Rola zmieniona na $newRole", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onToggleBlock = {
                        coroutineScope.launch {
                            try {
                                val resp = RetrofitClient.adminApi.toggleUserBlock(user.id)
                                if (resp.isSuccessful) {
                                    users = users.map { if (it.id == user.id) it.copy(blocked = !user.blocked) else it }
                                    Toast.makeText(context, if (!user.blocked) "Użytkownik zablokowany" else "Użytkownik odblokowany", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onDelete = {
                        coroutineScope.launch {
                            try {
                                val resp = RetrofitClient.adminApi.deleteUser(user.id)
                                if (resp.isSuccessful) {
                                    users = users.filter { it.id != user.id }
                                    Toast.makeText(context, "Użytkownik usunięty", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AdminUserRow(
    user: AdminUserResponse,
    onToggleRole: () -> Unit,
    onToggleBlock: () -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(user.id.toString(), color = Color.White, modifier = Modifier.weight(0.1f), fontSize = 12.sp)
            Text(user.username, color = Color.LightGray, modifier = Modifier.weight(0.25f), maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 12.sp)
            Text(user.email, color = Color.LightGray, modifier = Modifier.weight(0.3f), maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 12.sp)
            
            Surface(
                color = if (user.role == "ADMIN") Color.White else Color(0xFF2A3441),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.weight(0.15f)
            ) {
                Text(
                    text = user.role,
                    color = if (user.role == "ADMIN") Color.Black else Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    textAlign = TextAlign.Center
                )
            }
            
            Row(modifier = Modifier.weight(0.2f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onToggleRole, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Refresh, contentDescription = "Zmień rolę", tint = TextBlue, modifier = Modifier.size(16.dp))
                }
                IconButton(onClick = onToggleBlock, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = if (user.blocked) "Odblokuj" else "Zablokuj",
                        tint = if (user.blocked) Color(0xFF10B981) else Color(0xFFEF4444),
                        modifier = Modifier.size(16.dp)
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Usuń", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                }
            }
        }
        Divider(color = Color(0xFF2A3441))
    }
}

@Composable
fun AdminReviewsContent() {
    var reviews by remember { mutableStateOf<List<ReviewResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.adminApi.getReviews()
            if (response.isSuccessful) {
                reviews = response.body().orEmpty()
            } else {
                errorMessage = "Błąd ładowania: ${response.code()}"
            }
        } catch (e: Exception) {
            errorMessage = "Błąd połączenia: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Text("Moderacja recenzji", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(4.dp))
    Text("Przeglądaj i moderuj recenzje użytkowników", color = Color.Gray, fontSize = 14.sp)
    
    Spacer(modifier = Modifier.height(24.dp))
    
    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TextBlue)
            }
        }
        errorMessage != null -> {
            Text(errorMessage ?: "", color = MaterialTheme.colorScheme.error)
        }
        reviews.isEmpty() -> {
            Text("Brak recenzji do moderacji", color = Color.Gray)
        }
        else -> {
            reviews.forEach { review ->
                AdminReviewCard(
                    review = review,
                    onApprove = {
                        coroutineScope.launch {
                            try {
                                val resp = RetrofitClient.adminApi.approveReview(review.id)
                                if (resp.isSuccessful) {
                                    reviews = reviews.filter { it.id != review.id }
                                    Toast.makeText(context, "Recenzja zatwierdzona", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onDelete = {
                        coroutineScope.launch {
                            try {
                                val resp = RetrofitClient.adminApi.deleteReview(review.id)
                                if (resp.isSuccessful) {
                                    reviews = reviews.filter { it.id != review.id }
                                    Toast.makeText(context, "Recenzja usunięta", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun AdminReviewCard(
    review: ReviewResponse,
    onApprove: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Recenzja: ${review.movieTitle}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Użytkownik: ${review.username} • ${review.createdAt}", color = Color.Gray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(review.content, color = Color.LightGray, fontSize = 14.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onApprove,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2532), contentColor = Color.White),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Zatwierdź", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F1D1D), contentColor = Color.White),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Usuń", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AdminReportsContent() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var generatingReportTitle by remember { mutableStateOf<String?>(null) }

    fun generateReport(title: String, topRatedLimit: Int? = null) {
        generatingReportTitle = title
        coroutineScope.launch {
            val result = ReportDownloadManager.generateMovieReport(
                context = context,
                title = title,
                generatedBy = "Panel administratora",
                topRatedLimit = topRatedLimit
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

    Text("Generowanie raportow", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(4.dp))
    Text("Generuj i pobieraj raporty systemowe w formacie PDF", color = Color.Gray, fontSize = 14.sp)

    Spacer(modifier = Modifier.height(24.dp))

    AdminReportButton(
        title = "Top 100 filmow",
        isLoading = generatingReportTitle == "Top 100 filmow",
        enabled = generatingReportTitle == null,
        onClick = { generateReport("Top 100 filmow", topRatedLimit = 100) }
    )
    Spacer(modifier = Modifier.height(12.dp))
    AdminReportButton(
        title = "Raport aktywnosci",
        isLoading = generatingReportTitle == "Raport aktywnosci",
        enabled = generatingReportTitle == null,
        onClick = { generateReport("Raport aktywnosci") }
    )
    Spacer(modifier = Modifier.height(12.dp))
    AdminReportButton(
        title = "Statystyki gatunkow",
        isLoading = generatingReportTitle == "Statystyki gatunkow",
        enabled = generatingReportTitle == null,
        onClick = { generateReport("Statystyki gatunkow") }
    )
}

@Composable
fun AdminReportButton(
    title: String,
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().height(60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DarkBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}
