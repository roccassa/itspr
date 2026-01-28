package com.example.projecto_peli.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecto_peli.ViewModel.MovieViewModel
import com.example.projecto_peli.ui.theme.screens.AddEditMovieScreen
import com.example.projecto_peli.ui.theme.screens.DetailMovieScreen
import com.example.projecto_peli.ui.theme.screens.LoginScreen
import com.example.projecto_peli.ui.theme.screens.MovieListScreen
import com.example.projecto_peli.ui.theme.screens.RegisterScreen

@Composable
fun MovieApp(viewModel: MovieViewModel, onPickImage: () -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        // Pantalla de Login
        composable("login") {
            LoginScreen(
                onClickRegister = { navController.navigate("register") },
                onSuccessfulLogin = { navController.navigate("movie_list") }
            )
        }

        // Pantalla de Registro
        composable("register") {
            RegisterScreen(
                onClickBack = { navController.popBackStack() },
                onSuccessfulRegister = { navController.navigate("movie_list") }
            )
        }

        // Lista de películas
        composable("movie_list") {
            MovieListScreen(navController, viewModel)
        }

        // Pantalla para agregar nueva película
        composable("add_edit") {
            AddEditMovieScreen(
                navController = navController,
                movieId = null,
                onPickImage = onPickImage,
                viewModel = viewModel
            )
        }

        // Pantalla para editar película existente
        composable("add_edit/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")
            AddEditMovieScreen(
                navController = navController,
                movieId = movieId,
                onPickImage = onPickImage,
                viewModel = viewModel
            )
        }

        // Pantalla de detalle de película
        composable("detail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")
            DetailMovieScreen(
                navController = navController,
                movieId = movieId,
                viewModel = viewModel
            )
        }
    }
}

