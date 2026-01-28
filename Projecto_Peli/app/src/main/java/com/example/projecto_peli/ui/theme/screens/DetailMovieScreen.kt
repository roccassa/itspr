package com.example.projecto_peli.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.projecto_peli.ViewModel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMovieScreen(
    navController: NavController,
    movieId: String?,
    viewModel: MovieViewModel
) {
    val movies by viewModel.movies.collectAsState()
    val movie = movieId?.toIntOrNull()?.let { id ->
        movies.find { it.id == id }
    }

    movie?.let { selectedMovie ->
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(selectedMovie.title) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.Movie,
                                contentDescription = "Volver"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = if (selectedMovie.imageUri.isNotEmpty()) selectedMovie.imageUri else null,
                    contentDescription = selectedMovie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    placeholder = rememberVectorPainter(Icons.Default.Movie),
                    error = rememberVectorPainter(Icons.Default.Movie)
                )

                Spacer(Modifier.height(16.dp))

                Text("Sinopsis:", style = MaterialTheme.typography.titleMedium)
                Text(selectedMovie.synopsis)
                Spacer(Modifier.height(8.dp))

                Text("Calificación: ${selectedMovie.rating} ⭐")
                Spacer(Modifier.height(8.dp))

                Text("Reseña:", style = MaterialTheme.typography.titleMedium)
                Text(selectedMovie.review)
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text("Película no encontrada", style = MaterialTheme.typography.bodyLarge)
    }
}
