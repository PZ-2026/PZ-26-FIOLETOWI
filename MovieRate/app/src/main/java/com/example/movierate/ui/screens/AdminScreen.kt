package com.example.movierate.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import com.example.movierate.ui.components.DarkBackground
import com.example.movierate.ui.components.DarkSurface
import com.example.movierate.ui.components.TextBlue

enum class AdminCategory(
    val title: String, 
    val color: Color, 
    val icon: ImageVector,
    val count: String,
    val subtitle: String
) {
    USERS("Użytkownicy", Color(0xFF3B82F6), Icons.Default.Person, "8,234", "+124 w tym miesiącu"),
    MOVIES("Filmy", Color(0xFF10B981), Icons.Default.PlayArrow, "12,547", "+45 w tym tygodniu"),
    REVIEWS("Recenzje", Color(0xFF9333EA), Icons.Default.Email, "45,891", "+234 dzisiaj"),
    SYSTEM("System", Color(0xFFF59E0B), Icons.Default.Settings, "OK", "Wszystko działa")
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
                // Using generic Shield/Lock substitute if Security is missing, Lock is everywhere
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
                        AdminCategory.SYSTEM -> AdminSystemContent()
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
                Text(category.count, color = if (category == AdminCategory.SYSTEM) category.color else Color.White, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(category.subtitle, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun AdminMoviesContent() {
    Text("Zarządzanie filmami", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(4.dp))
    Text("Dodaj, edytuj lub usuń filmy z bazy danych", color = Color.Gray, fontSize = 14.sp)
    
    Spacer(modifier = Modifier.height(24.dp))
    
    AdminTextField("Tytuł", "Wprowadź tytuł filmu")
    Spacer(modifier = Modifier.height(16.dp))
    AdminTextField("Rok produkcji", "2024")
    Spacer(modifier = Modifier.height(16.dp))
    AdminTextField("Opis", "Wprowadź opis filmu", minLines = 3)
    Spacer(modifier = Modifier.height(16.dp))
    
    // Type Dropdown mock
    Text("Typ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = "Film",
        onValueChange = {},
        readOnly = true,
        trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkBackground,
            unfocusedBorderColor = DarkBackground,
            focusedContainerColor = DarkBackground,
            unfocusedContainerColor = DarkBackground,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    )
    
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
    
    Spacer(modifier = Modifier.height(32.dp))
    
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Dodaj film", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AdminTextField(label: String, placeholder: String, minLines: Int = 1) {
    Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = placeholder,
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        minLines = minLines,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkBackground,
            unfocusedBorderColor = DarkBackground,
            focusedContainerColor = DarkBackground,
            unfocusedContainerColor = DarkBackground,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.Gray
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun AdminUsersContent() {
    Text("Zarządzanie użytkownikami", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(4.dp))
    Text("Przeglądaj i zarządzaj kontami użytkowników", color = Color.Gray, fontSize = 14.sp)
    
    Spacer(modifier = Modifier.height(24.dp))
    
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("ID", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.1f))
        Text("Nazwa", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.35f))
        Text("Email", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.4f))
        Text("Rola", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.15f))
    }
    Divider(color = Color(0xFF2A3441))
    
    UserRow("1", "Jan Kowalski", "jan@example.com", "USER", false)
    UserRow("2", "Anna Nowak", "anna@example.com", "USER", false)
    UserRow("3", "Admin Test", "admin@example.com", "ADMIN", true)
    
    Spacer(modifier = Modifier.height(16.dp))
    // Fake scrollbar visual
    Box(
        modifier = Modifier.fillMaxWidth().height(8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            Box(modifier = Modifier.weight(1f).height(4.dp).background(Color(0xFF2A3441), CircleShape)) {
                Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.6f).background(Color.Gray, CircleShape))
            }
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun UserRow(id: String, name: String, email: String, role: String, isAdmin: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(id, color = Color.White, modifier = Modifier.weight(0.1f))
        Text(name, color = Color.LightGray, modifier = Modifier.weight(0.35f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(email, color = Color.LightGray, modifier = Modifier.weight(0.4f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        
        Surface(
            color = if (isAdmin) Color.White else Color(0xFF2A3441),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.weight(0.15f)
        ) {
            Text(
                text = role,
                color = if (isAdmin) Color.Black else Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
    Divider(color = Color(0xFF2A3441))
}

@Composable
fun AdminReviewsContent() {
    Text("Moderacja recenzji", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(4.dp))
    Text("Przeglądaj i moderuj recenzje użytkowników", color = Color.Gray, fontSize = 14.sp)
    
    Spacer(modifier = Modifier.height(24.dp))
    
    ReviewModCard("Nieodpowiednia treść w recenzji", "Film: Breaking Bad • Użytkownik: user123", "Recenzja zawiera wulgaryzmy...")
    Spacer(modifier = Modifier.height(16.dp))
    ReviewModCard("Spam w recenzji", "Film: Incepcja • Użytkownik: spammer99", "Link do zewnętrznej...")
}

@Composable
fun ReviewModCard(title: String, subtitle: String, contentPreview: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, color = Color.Gray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(contentPreview, color = Color.LightGray, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2532), contentColor = Color.White),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("Zatwierdź", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444), contentColor = Color.White),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("Usuń", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AdminSystemContent() {
    Text("Generowanie raportów", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(4.dp))
    Text("Generuj i pobieraj raporty systemowe w formacie PDF", color = Color.Gray, fontSize = 14.sp)
    
    Spacer(modifier = Modifier.height(24.dp))
    
    AdminReportButton("Top 100 filmów")
    Spacer(modifier = Modifier.height(12.dp))
    AdminReportButton("Raport aktywności")
    Spacer(modifier = Modifier.height(12.dp))
    AdminReportButton("Statystyki gatunków")
}

@Composable
fun AdminReportButton(title: String) {
    Button(
        onClick = { },
        modifier = Modifier.fillMaxWidth().height(60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DarkBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}
