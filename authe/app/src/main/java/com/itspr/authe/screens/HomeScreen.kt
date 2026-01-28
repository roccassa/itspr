package com.itspr.authe.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itspr.authe.viewmodel.AuthState
import com.itspr.authe.viewmodel.AuthViewModel

@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel){

    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        if (authState.value !is AuthState.Authenticated) {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pagina principal", fontSize = 25.sp, color = Color(0xFF000000))
        Spacer(modifier = Modifier.height(100.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                Button(onClick = {
                    navController.navigate("list")
                }) {
                    Text(text = "Abrir Apis")
                }
            }

            Column {
                Button(onClick = {
                    navController.navigate("")
                }) {
                    Text(text = "Abrir FireScreen")
                }
            }
        }

        Spacer(modifier = Modifier.height(90.dp))

        TextButton(onClick = {
            authViewModel.signout()
            navController.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }) {
            Text(text = "Cerrar sesión")
        }
    }
}