package com.example.projecto_peli.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.projecto_peli.ViewModel.MovieViewModel
import com.example.projecto_peli.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(navController: NavController, viewModel: MovieViewModel) {
    val movies by viewModel.movies.collectAsState()
    val searchQuery by viewModel.searchQuery
    val sortByRating by viewModel.sortByRating
    val sortDescending by viewModel.sortDescending

    Scaffold(
        topBar = {
            Column {
                Spacer(Modifier.height(30.dp))
                Text("Cineteca Digital", textAlign = TextAlign.Center, modifier = Modifier.padding(20.dp).fillMaxWidth())
                OutlinedTextField(
                    value = searchQuery,
                    label = { Text("Buscar") },
                    onValueChange = viewModel::updateSearchQuery,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FilterChip(
                        selected = sortByRating,
                        onClick = { viewModel.toggleSortByRating() },
                        label = { Text("Calificación") },
                        leadingIcon = {
                            Icon(Icons.Filled.Star, null, tint = if (sortByRating) MaterialTheme.colorScheme.primary else LocalContentColor.current)
                        }
                    )
                    FilterChip(
                        selected = !sortByRating,
                        onClick = { viewModel.toggleSortByRating() },
                        label = { Text("Título") }
                    )
                    IconButton(onClick = { viewModel.toggleSortDirection() }) {
                        Icon(imageVector = if (sortDescending) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward, contentDescription = "Orden")
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.clearForm()
                navController.navigate("add_edit")
            }) { Icon(Icons.Filled.Add, "Agregar") }
        }
    ) { padding ->
        if (movies.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(if (searchQuery.isEmpty()) "No hay películas" else "No se encontraron resultados", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(movies) { movie ->
                    MovieListItem(
                        movie = movie,
                        onClick = { navController.navigate("detail/${movie.id}") },
                        onEdit = {
                            viewModel.setMovie(movie)
                            navController.navigate("add_edit")
                        },
                        onDelete = { viewModel.deleteMovie(movie) }
                    )
                }
            }
        }
    }
}

@Composable
fun MovieListItem(movie: Movie, onClick: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = movie.imageUri.ifEmpty { null }, contentDescription = null, modifier = Modifier.size(60.dp), placeholder = null)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(movie.title, style = MaterialTheme.typography.titleMedium)
                Text("${movie.rating} ⭐", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onEdit) { Icon(Icons.Filled.Edit, "Editar") }
            IconButton(onClick = onDelete) { Icon(Icons.Filled.Delete, "Eliminar") }
        }
    }
}
