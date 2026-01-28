package com.itspr.micro.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.itspr.micro.model.Destinatario
import com.itspr.micro.viewmodel.ShipmentManagerViewModel
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipientListScreen(
    navController: NavHostController,
    viewModel: ShipmentManagerViewModel
) {
    val recipients = viewModel.recipients.collectAsState().value
    val message = viewModel.message.collectAsState().value

    // Recargar la lista cada vez que se entra a la pantalla para reflejar cambios
    LaunchedEffect(Unit) {
        viewModel.loadRecipients()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Ver Destinatarios") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // NAVEGACIÓN PARA AGREGAR: Pasamos "null" como JSON
                navController.navigate("recipient_add")  // ← Ruta limpia, sin "null"
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar destinatario")
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
            message?.let { Text(it, modifier = Modifier.padding(8.dp)) }

            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                items(recipients) { recipient ->
                    RecipientListItem(
                        recipient = recipient,
                        onEdit = {
                            // NAVEGACIÓN PARA EDITAR: Serializamos el objeto
                            val recipientJson = URLEncoder.encode(Gson().toJson(recipient), "UTF8")
                            navController.navigate("recipient_edit/$recipientJson")
                        },
                        onDelete = { viewModel.deleteRecipient(recipient.id) }
                    )
                }
            }
        }
    }
}

// Item Composable (sin cambios en la estructura interna)
@Composable
fun RecipientListItem(
    recipient: Destinatario,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Nombre: ${recipient.nombre}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "Dirección: ${recipient.direccion}, ${recipient.ciudad}, ${recipient.estado}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "CP: ${recipient.codigoPostal}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.width(8.dp))

            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}