package com.itspr.micro.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.itspr.micro.model.Destinatario
import com.itspr.micro.model.Envio
import com.itspr.micro.viewmodel.ShipmentManagerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipmentPrintScreen(
    navController: NavHostController,
    viewModel: ShipmentManagerViewModel,
    // Recibe el objeto Envio ya deserializado del NavGraph
    shipmentToPrint: Envio
) {
    // Estados para la data combinada y la carga
    var recipient by remember { mutableStateOf<Destinatario?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Lógica para buscar el Destinatario
    LaunchedEffect(shipmentToPrint.claveDestinatario) {
        scope.launch {
            isLoading = true
            // Usamos la función del ViewModel para buscar por clave
            recipient = viewModel.getRecipientByClave(shipmentToPrint.claveDestinatario)
            isLoading = false
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Vista Previa de Impresión") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(32.dp))
                    Text("Cargando detalles del destinatario...")
                }
                recipient == null -> {
                    Text(
                        text = "Error: No se encontró el destinatario con la clave ${shipmentToPrint.claveDestinatario}.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(32.dp)
                    )
                }
                else -> {
                    // Muestra el Documento Formateado
                    PrintableDocumentCard(shipment = shipmentToPrint, recipient = recipient!!)

                    Button(
                        onClick = {
                            viewModel.sendMessage("Simulación: Se ha generado el documento para imprimir.")
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(top = 24.dp, bottom = 24.dp)
                    ) {
                        Text("Imprimir Documento")
                    }
                }
            }
        }
    }
}

// --- Composable para el Diseño del Documento ---
@Composable
fun PrintableDocumentCard(shipment: Envio, recipient: Destinatario) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("DOCUMENTO DE ENVÍO", style = MaterialTheme.typography.headlineMedium)
            Divider(Modifier.padding(vertical = 10.dp))

            // --- SECCIÓN ENVÍO ---
            Text("DATOS DEL ENVÍO", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            DataRow("ID Envío:", shipment.id)
            DataRow("Clave Destinatario:", shipment.claveDestinatario)
            DataRow("Producto:", shipment.descripcionProducto)
            DataRow("Fecha de Envío:", shipment.fechaEnvio)

            Divider(Modifier.padding(vertical = 16.dp))

            // --- SECCIÓN DESTINATARIO ---
            Text("DATOS DEL DESTINATARIO", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            DataRow("Nombre:", recipient.nombre)
            DataRow("Dirección:", "${recipient.direccion}, CP ${recipient.codigoPostal}")
            DataRow("Ciudad/Estado:", "${recipient.ciudad}, ${recipient.estado}")
            DataRow("Teléfono:", "[FALTA CAMPO TELÉFONO EN EL MODELO]") // Recordatorio
        }
    }
}

// Componente helper para filas de datos
@Composable
fun DataRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier.weight(0.4f)
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(0.6f)
        )
    }
}