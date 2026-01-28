package com.itspr.parcial2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.itspr.parcial2.model.Character
import com.itspr.parcial2.screens.CharacterDetailScreen
import com.itspr.parcial2.screens.CharacterListScreen
import com.itspr.parcial2.viewmodel.CharacterViewModel
import java.net.URLDecoder

@Composable
fun NavGraph(viewModel: CharacterViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") { CharacterListScreen(viewModel = viewModel, navController = navController)}
        composable("detail/{characterJson}", arguments = listOf(navArgument("characterJson") { type =
            NavType.StringType })
        ){ backStackEntry ->
            val characterJson = URLDecoder.decode(backStackEntry.arguments?.getString("characterJson"), "UTF8")
            val character = Gson().fromJson(characterJson, Character::class.java)
            CharacterDetailScreen(character)
        }

    }//navhost
}//navgraph