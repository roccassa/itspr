package com.example.projecto_peli

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.projecto_peli.ViewModel.MovieViewModel
import com.example.projecto_peli.navigation.MovieApp
import com.example.projecto_peli.repository.MovieRepository
import com.example.projecto_peli.ui.theme.Projecto_PeliTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MovieViewModel
    private lateinit var repository: MovieRepository

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    // Ignorar si ya tiene permiso
                }
                viewModel.updateImageUri(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear directamente el repositorio
        repository = MovieRepository()
        viewModel = MovieViewModel(repository)

        setContent {
            Projecto_PeliTheme {
                MovieApp(
                    viewModel = viewModel,
                    onPickImage = { pickImageLauncher.launch("image/*") }
                )
            }
        }
    }
}
