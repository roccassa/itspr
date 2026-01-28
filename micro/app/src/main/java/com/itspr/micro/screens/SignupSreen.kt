package com.itspr.micro.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itspr.micro.R
import com.itspr.micro.viewmodel.AuthState
import com.itspr.micro.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun signupScreen(navController: NavController, authViewModel: AuthViewModel) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState by authViewModel.authState.observeAsState()
    val context = LocalContext.current

    // Efecto para manejar navegación y errores
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.VerificationSent -> {
                Toast.makeText(
                    context,
                    "¡Cuenta creada! Revisa tu email para verificarla antes de iniciar sesión.",
                    Toast.LENGTH_LONG
                ).show()
                navController.navigate("login") {
                    popUpTo("signup") { inclusive = true }
                }
            }

            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState as AuthState.Error).mensaje,
                    Toast.LENGTH_LONG
                ).show()
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

            // Logo o título grande
            Image(
                painter = painterResource(id = R.drawable.logo_envio),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp)
            )

            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Regístrate para continuar",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón principal
            Button(
                onClick = { authViewModel.signup(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("Crear cuenta", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ir al login
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¿Ya tienes cuenta? ", color = MaterialTheme.colorScheme.onSurfaceVariant)
                TextButton(onClick = { navController.navigate("login") }) {
                    Text(
                        "Inicia sesión",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                        )
                    )
                }
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
