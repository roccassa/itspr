package com.itspr.authe.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itspr.authe.viewmodel.AuthState
import com.itspr.authe.viewmodel.AuthViewModel
import kotlin.math.atan

@Composable
fun SignupScreen(navController: NavController, authViewModel: AuthViewModel){


    //para auth
    var email by remember { mutableStateOf("") }
    var epassword by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current



    Box(modifier = Modifier){
        /*Image(
            painter = painterResource(id =com.itspr.authe.R.drawable.tecnm_logo),
            contentDescription = "fondo_login",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.15f
        )*/
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            Text(text = "Sign Up", fontSize = 25.sp, color = Color(0xFF000000))
            //Text(text = "PIZZERIA TECNM", fontSize = 25.sp, color = Color(0xFF000000))
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                label = {Text(text = "Email")}
            )

            Spacer(modifier = Modifier.height(38.dp))

            OutlinedTextField(
                value = epassword,
                onValueChange = {epassword = it},
                label = {Text(text = "Password")},
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                authViewModel.signup(email, epassword)
            }) {
                Text(text = "Crear cuenta")
            }

            Spacer(modifier = Modifier.height(40.dp))

            TextButton(onClick = {
                navController.navigate("login")
            }) {
                Text(text = "¿Ya tienes cuenta? Inicia sesión")
            }

        }//column


        LaunchedEffect(authState.value) {
            when(authState.value){
                is AuthState.Authenticated -> {
                    authViewModel.resetAuthState()
                    Toast.makeText(context, "✅ Cuenta creada exitosamente.", Toast.LENGTH_LONG).show()
                    email = ""
                    epassword = ""
                    navController.navigate("login"){
                        popUpTo("signup") { inclusive = true }
                    }
                }

                else -> Unit
            }
        }

        //boton de exit
        val activity = (LocalContext.current as? Activity)
        Button(onClick = {
            activity?.finish()
        },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        ) {
            Text(text = "EXIT")
        }
    }//box
}//fun
