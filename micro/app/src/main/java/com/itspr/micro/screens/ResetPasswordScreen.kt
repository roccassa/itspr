package com.itspr.micro.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itspr.micro.viewmodel.AuthState
import com.itspr.micro.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    val authState by authViewModel.authState.observeAsState()
    val context = LocalContext.current

    // Manejo de estados
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.PasswordResetSent -> {
                Toast.makeText(
                    context,
                    "Se ha enviado un correo a $email. ¡Revisa tu bandeja de entrada!",
                    Toast.LENGTH_LONG
                ).show()
                authViewModel.resetAuthState()
                navController.navigate("login") {
                    popUpTo("forgotPassword") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState as AuthState.Error).mensaje,
                    Toast.LENGTH_LONG
                ).show()
                authViewModel.resetAuthState()
            }
            else -> Unit
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Restablecer Contraseña",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Ingresa tu correo para recibir un enlace de recuperación",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { authViewModel.sendPasswordReset(email) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("Enviar correo de restablecimiento", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { navController.navigate("login") }) {
                Text(
                    "Cancelar y volver al Login",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Botón EXIT en la parte inferior
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val activity = (LocalContext.current as? Activity)
            TextButton(
                onClick = { activity?.finish() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                Text("SALIR DE LA APP", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
