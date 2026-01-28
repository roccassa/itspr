package com.example.projecto_peli.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.projecto_peli.ViewModel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMovieScreen(
    navController: NavController,
    movieId: String?,
    onPickImage: () -> Unit,
    viewModel: MovieViewModel
) {
    val title by viewModel.title
    val synopsis by viewModel.synopsis
    val review by viewModel.review
    val rating by remember { mutableStateOf(viewModel.rating.value) }
    val imageUri by remember { mutableStateOf(viewModel.imageUri.value) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (viewModel.currentMovie.value == null) "Nueva Película" else "Editar Película") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(value = title, onValueChange = { viewModel.title.value = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = synopsis, onValueChange = { viewModel.synopsis.value = it }, label = { Text("Sinopsis") }, minLines = 3, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Text("Calificación: ${viewModel.rating.value.toInt()}")
            StarRatingBar(rating = viewModel.rating.value, onRatingChanged = { viewModel.rating.value = it })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = review, onValueChange = { viewModel.review.value = it }, label = { Text("Reseña") }, minLines = 3, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            Button(onClick = onPickImage, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.Image, "Imagen")
                Spacer(Modifier.width(8.dp))
                Text("Seleccionar Imagen")
            }
            imageUri?.let {
                AsyncImage(model = it, contentDescription = null, modifier = Modifier.fillMaxWidth().height(200.dp).padding(8.dp))
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                viewModel.saveMovie()
                navController.popBackStack()
            }, modifier = Modifier.fillMaxWidth()) { Text("Guardar") }
        }
    }
}
