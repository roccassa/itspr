package com.itspr.authe.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
//import com.itspr.authe.screens.CharacterDetailScreen
//import com.itspr.authe.screens.CharacterListScreen
//import com.itspr.authe.viewmodel.CharacterViewModel
import com.itspr.authe.model.Character
import com.itspr.authe.model.Product
import com.itspr.authe.screens.CalculadoraScreen
import com.itspr.authe.screens.ProductListScreen
import com.itspr.authe.screens.ProductDetailScreen
import com.itspr.authe.screens.HomeScreen
import com.itspr.authe.screens.LoginScreen
import com.itspr.authe.screens.SignupScreen
import com.itspr.authe.viewmodel.AuthState
import com.itspr.authe.viewmodel.AuthViewModel
import com.itspr.authe.viewmodel.ProductViewModel
import java.net.URLDecoder
/*
@Composable
fun NavGraph(characterViewModel: CharacterViewModel,authViewModel: AuthViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("list") { CharacterListScreen(viewModel = characterViewModel, navController = navController)}
        composable("detail/{characterJson}", arguments = listOf(navArgument("characterJson") { type =
            NavType.StringType })
        ){ backStackEntry ->
            val characterJson = URLDecoder.decode(backStackEntry.arguments?.getString("characterJson"), "UTF8")
            val character = Gson().fromJson(characterJson, Character::class.java)
            CharacterDetailScreen(character)
        }

        composable("login"){ LoginScreen(navController, authViewModel)}
        composable("signup"){ SignupScreen(navController, authViewModel) }
        composable("home"){ HomeScreen(navController, authViewModel) }

    }//navhost
}//navgraph
*/


@Composable
fun NavGraph(productViewModel: ProductViewModel, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "calculadora") {

        // Lista de productos
        composable("list") {
            ProductListScreen(viewModel = productViewModel, navController = navController)
        }

        // Detalle de producto
        composable(
            "detail/{productJson}",
            arguments = listOf(navArgument("productJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val productJson = URLDecoder.decode(
                backStackEntry.arguments?.getString("productJson"),
                "UTF8"
            )
            val product = Gson().fromJson(productJson, Product::class.java)
            ProductDetailScreen(product)
        }

        // Pantallas de autenticación
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("signup") { SignupScreen(navController, authViewModel) }
        composable("home") { HomeScreen(navController, authViewModel) }

        composable("calculadora") { CalculadoraScreen(navController) }



    }
}
