package com.itspr.micro.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.itspr.micro.model.Envio
import com.itspr.micro.viewmodel.ShipmentManagerViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipmentAddEditScreen(
    navController: NavHostController,
    viewModel: ShipmentManagerViewModel,
    originalEnvio: Envio?
) {
    // Campos de estado inicializados con el objeto existente o vacíos si es nuevo
    var id by remember { mutableStateOf(originalEnvio?.id ?: "") }
    var claveDestinatario by remember { mutableStateOf(originalEnvio?.claveDestinatario ?: "") }
    var destinatarioEncontrado by remember { mutableStateOf<String?>(originalEnvio?.nombreDestinatario) }
    var descripcion by remember { mutableStateOf(originalEnvio?.descripcionProducto ?: "") }
    var fechaEnvio by remember { mutableStateOf(originalEnvio?.fechaEnvio ?: "") }

    var tipoEnvio by remember { mutableStateOf(originalEnvio?.tipoEnvio ?: "Estándar") }
    var expanded by remember { mutableStateOf(false) }

    val isEditing = originalEnvio != null
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (isEditing) "Editar Envío" else "Agregar Envío") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- CAMPO CLAVE DE DESTINATARIO ---
            OutlinedTextField(
                value = claveDestinatario,
                onValueChange = { newValue ->
                    // Limitamos y guardamos el valor
                    if (newValue.length <= 5 && newValue.all { it.isLetterOrDigit() }) {
                        claveDestinatario = newValue.uppercase()
                        destinatarioEncontrado = null // Limpiar si cambia la clave

                        // Si se completa la clave, buscar el destinatario
                        if (claveDestinatario.length == 5) {
                            scope.launch {
                                val found = viewModel.getRecipientByClave(claveDestinatario)
                                destinatarioEncontrado = found?.nombre

                                if (found == null) {
                                    viewModel.sendMessage("Clave no encontrada. Por favor verifique.")                                }
                            }
                        }
                    }
                },
                label = { Text("Clave de Destinatario (5 dígitos)") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- MOSTRAR NOMBRE DEL DESTINATARIO ENCONTRADO ---
            if (destinatarioEncontrado != null) {
                Text(
                    text = "Destinatario: **$destinatarioEncontrado**",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (claveDestinatario.length == 5 && destinatarioEncontrado == null) {
                Text(
                    text = "Clave no encontrada. Por favor verifique.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }



            // Campo Descripción del Producto
            OutlinedTextField(value = descripcion, onValueChange = { descripcion = it },
                label = { Text("Descripción del Producto") }, modifier = Modifier.fillMaxWidth())

            // Campo Fecha de Envío
            OutlinedTextField(value = fechaEnvio, onValueChange = { fechaEnvio = it },
                label = { Text("Fecha de Envío (ej. YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = when (tipoEnvio) {
                        "DiaSiguiente" -> "Día siguiente"
                        "Express" -> "Express"
                        else -> "Estándar"
                    },
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Tipo de envío") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()  // ← IMPORTANTE: sin esto no funciona
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf("Estándar", "Express", "Día siguiente").forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                tipoEnvio = when (opcion) {
                                    "Día siguiente" -> "DiaSiguiente"
                                    "Express" -> "Express"
                                    else -> "Estándar"
                                }
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val envioToSave = Envio(
                        id = id,
                        claveDestinatario = claveDestinatario, // USAR LA CLAVE
                        descripcionProducto = descripcion,
                        fechaEnvio = fechaEnvio,
                        nombreDestinatario = destinatarioEncontrado, //  GUARDAR EL NOMBRE EN EL ENVÍO
                        tipoEnvio = tipoEnvio
                    )
                    viewModel.saveShipment(envioToSave)
                    navController.popBackStack()
                },
                enabled = destinatarioEncontrado != null && descripcion.isNotBlank() && fechaEnvio.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(if (isEditing) "Guardar Cambios" else "Agregar Envío")
            }
        }
    }
}