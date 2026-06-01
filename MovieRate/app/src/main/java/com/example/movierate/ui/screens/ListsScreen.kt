package com.example.movierate.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movierate.data.remote.AuthResponse
import com.example.movierate.data.remote.CreateListRequest
import com.example.movierate.data.remote.RetrofitClient
import com.example.movierate.data.remote.UserListItemResponse
import com.example.movierate.data.remote.UserListResponse
import com.example.movierate.model.Movie
import com.example.movierate.ui.components.DarkBackground
import com.example.movierate.ui.components.DarkSurface
import com.example.movierate.ui.components.MovieCardWithImage
import com.example.movierate.ui.components.TextBlue
import kotlinx.coroutines.launch

data class SystemListInfo(
    val type: String,
    val displayName: String,
    val icon: ImageVector,
    val color: Color
)

private val systemListTypes = listOf(
    SystemListInfo("WATCHLIST", "Do obejrzenia", Icons.Default.DateRange, Color(0xFF3B82F6)),
    SystemListInfo("WATCHED", "Obejrzane", Icons.Default.Check, Color(0xFF10B981)),
    SystemListInfo("FAVORITES", "Ulubione", Icons.Default.FavoriteBorder, Color(0xFFEF4444))
)

@Composable
fun ListsScreen(
    modifier: Modifier = Modifier,
    user: AuthResponse? = null,
    onNavigateToMovieDetail: (Movie) -> Unit = {}
) {
    var userLists by remember { mutableStateOf<List<UserListResponse>>(emptyList()) }
    var selectedListId by remember { mutableStateOf<Long?>(null) }
    var listItems by remember { mutableStateOf<List<UserListItemResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showCustomLists by remember { mutableStateOf(false) }
    var showCreateListDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf<UserListResponse?>(null) }
    var showDeleteConfirm by remember { mutableStateOf<UserListResponse?>(null) }
    var newListName by remember { mutableStateOf("") }
    var renameText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val userId = user?.userId ?: 0L

    // Load user lists
    LaunchedEffect(user) {
        if (user == null || userId == 0L) return@LaunchedEffect
        isLoading = true
        try {
            val listsResponse = RetrofitClient.listsApi.getUserLists(userId = userId)
            if (listsResponse.isSuccessful) {
                userLists = listsResponse.body().orEmpty()
                // Auto-select first system list
                val watchlist = userLists.find { it.type == "WATCHLIST" }
                if (watchlist != null && selectedListId == null) {
                    selectedListId = watchlist.id
                }
            }
        } catch (e: Exception) {
            errorMessage = "Blad ladowania list: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    // Load items for selected list
    LaunchedEffect(selectedListId) {
        val listId = selectedListId ?: return@LaunchedEffect
        try {
            val itemsResponse = RetrofitClient.listsApi.getListItems(listId)
            if (itemsResponse.isSuccessful) {
                listItems = itemsResponse.body().orEmpty()
            }
        } catch (e: Exception) {
            errorMessage = "Blad ladowania elementow: ${e.message}"
        }
    }

    // Create list dialog
    if (showCreateListDialog) {
        AlertDialog(
            onDismissRequest = { showCreateListDialog = false; newListName = "" },
            title = { Text("Nowa lista", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newListName,
                    onValueChange = { newListName = it },
                    label = { Text("Nazwa listy", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface,
                        cursorColor = TextBlue
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newListName.isNotBlank()) {
                            coroutineScope.launch {
                                try {
                                    RetrofitClient.listsApi.createList(
                                        CreateListRequest(userId, newListName, "CUSTOM")
                                    )
                                    val listsResponse = RetrofitClient.listsApi.getUserLists(userId = userId)
                                    if (listsResponse.isSuccessful) {
                                        userLists = listsResponse.body().orEmpty()
                                    }
                                } catch (_: Exception) { }
                                showCreateListDialog = false
                                newListName = ""
                            }
                        }
                    },
                    enabled = newListName.isNotBlank()
                ) {
                    Text("Utworz", color = TextBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateListDialog = false; newListName = "" }) {
                    Text("Anuluj", color = Color.Gray)
                }
            },
            containerColor = DarkSurface
        )
    }

    // Rename dialog
    showRenameDialog?.let { list ->
        AlertDialog(
            onDismissRequest = { showRenameDialog = null },
            title = { Text("Zmień nazwę", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    label = { Text("Nowa nazwa", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface,
                        cursorColor = TextBlue
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (renameText.isNotBlank()) {
                            coroutineScope.launch {
                                try {
                                    RetrofitClient.listsApi.renameList(list.id, mapOf("name" to renameText))
                                    val listsResponse = RetrofitClient.listsApi.getUserLists(userId = userId)
                                    if (listsResponse.isSuccessful) {
                                        userLists = listsResponse.body().orEmpty()
                                    }
                                } catch (_: Exception) { }
                                showRenameDialog = null
                            }
                        }
                    },
                    enabled = renameText.isNotBlank()
                ) {
                    Text("Zapisz", color = TextBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = null }) {
                    Text("Anuluj", color = Color.Gray)
                }
            },
            containerColor = DarkSurface
        )
    }

    // Delete confirm dialog
    showDeleteConfirm?.let { list ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("Usun liste", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text("Czy na pewno chcesz usunac liste \"${list.name}\"?", color = Color.Gray) },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                RetrofitClient.listsApi.deleteList(list.id)
                                val listsResponse = RetrofitClient.listsApi.getUserLists(userId = userId)
                                if (listsResponse.isSuccessful) {
                                    userLists = listsResponse.body().orEmpty()
                                    // If deleted list was selected, switch to first system list
                                    if (selectedListId == list.id) {
                                        val firstSystem = userLists.find { it.type == "WATCHLIST" }
                                        selectedListId = firstSystem?.id
                                    }
                                }
                            } catch (_: Exception) { }
                            showDeleteConfirm = null
                        }
                    }
                ) {
                    Text("Usun", color = Color(0xFFEF4444))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = null }) {
                    Text("Anuluj", color = Color.Gray)
                }
            },
            containerColor = DarkSurface
        )
    }

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(12.dp))
        Text("Moje Listy", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(12.dp))

        if (showCustomLists) {
            // ===== CUSTOM LISTS VIEW =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Moje listy", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Row {
                    IconButton(onClick = { showCreateListDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Utworz liste", tint = TextBlue)
                    }
                    IconButton(onClick = { showCustomLists = false }) {
                        Icon(Icons.Default.List, contentDescription = "Listy systemowe", tint = Color.Gray)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            val customLists = userLists.filter { it.type == "CUSTOM" }

            if (customLists.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Brak wlasnych list", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showCreateListDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = TextBlue)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Utworz liste")
                        }
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(customLists) { list ->
                        CustomListCard(
                            list = list,
                            isSelected = selectedListId == list.id,
                            onClick = {
                                selectedListId = list.id
                                showCustomLists = false
                            },
                            onRename = {
                                renameText = list.name
                                showRenameDialog = list
                            },
                            onDelete = {
                                showDeleteConfirm = list
                            }
                        )
                    }
                }
            }
        } else {
            // ===== SYSTEM LISTS VIEW =====
            // System list type selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                systemListTypes.forEach { sysList ->
                    val list = userLists.find { it.type == sysList.type }
                    val isSelected = list != null && selectedListId == list.id
                    SystemListChip(
                        label = sysList.displayName,
                        icon = sysList.icon,
                        color = sysList.color,
                        count = list?.itemCount ?: 0,
                        isSelected = isSelected,
                        onClick = { list?.let { selectedListId = it.id } },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Custom lists button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { showCustomLists = true }) {
                    Icon(Icons.Default.List, contentDescription = null, tint = TextBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Moje listy", color = TextBlue, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Movie grid
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = TextBlue)
                    }
                }
                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(errorMessage ?: "", color = MaterialTheme.colorScheme.error)
                    }
                }
                selectedListId == null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Wybierz liste", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
                listItems.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Brak elementow w tej liscie", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(listItems) { item ->
                            MovieCardWithImage(
                                movie = Movie(
                                    id = item.movieId,
                                    title = item.movieTitle,
                                    description = "",
                                    rating = item.averageRating,
                                    year = item.releaseYear ?: 0,
                                    type = if (item.type == "SERIES") "Serial" else "Film",
                                    imageUrl = item.imageUrl ?: "https://picsum.photos/seed/movie${item.movieId}/300/450"
                                ),
                                onClick = {
                                    onNavigateToMovieDetail(
                                        Movie(
                                            id = item.movieId,
                                            title = item.movieTitle,
                                            description = "",
                                            rating = item.averageRating,
                                            year = item.releaseYear ?: 0,
                                            type = if (item.type == "SERIES") "Serial" else "Film",
                                            imageUrl = item.imageUrl ?: "https://picsum.photos/seed/movie${item.movieId}/300/450"
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SystemListChip(
    label: String,
    icon: ImageVector,
    color: Color,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) color.copy(alpha = 0.2f) else DarkSurface,
        border = if (isSelected) BorderStroke(1.5.dp, color) else null
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = if (isSelected) color else Color.Gray, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, color = if (isSelected) color else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 11.sp, maxLines = 1)
            Text(count.toString(), color = if (isSelected) color else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun CustomListCard(
    list: UserListResponse,
    isSelected: Boolean,
    onClick: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) TextBlue.copy(alpha = 0.15f) else DarkSurface
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) BorderStroke(1.5.dp, TextBlue) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(list.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("${list.itemCount} elementow", color = Color.Gray, fontSize = 13.sp)
            }
            Row {
                IconButton(onClick = onRename, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Zmień nazwę", tint = TextBlue, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Usun", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
