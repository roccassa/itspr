package com.itspr.micro.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.itspr.micro.viewmodel.AuthState
import com.itspr.micro.viewmodel.AuthViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun homeScreen(navController: NavController, authViewModel: AuthViewModel) {

    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        if (authState.value !is AuthState.Authenticated) {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Micro Logistics",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // Icono grande: camión de envíos
            Icon(
                imageVector = Icons.Filled.LocalShipping,   // EXISTE COMO FILLED
                contentDescription = "Envíos",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(80.dp)
            )

            Text("Bienvenido", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                "Gestiona envíos de manera eficiente",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjetas con iconos que SÍ existen
            ActionCard("Mi Perfil", Icons.Filled.Person){ navController.navigate("profile") }
            ActionCard("Códigos Postales",  Icons.Filled.LocationOn) {navController.navigate("zipCode") }
            ActionCard("Gestión de Envíos", Icons.Filled.Inventory2){ navController.navigate("shipment_crud_home") }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = {
                    authViewModel.signout()
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión", fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ActionCard(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(20.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null,
                tint = MaterialTheme.colorScheme.outline)
        }
    }
}