package com.itspr.micro.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.itspr.micro.model.Destinatario
import androidx.compose.runtime.rememberCoroutineScope
import com.itspr.micro.viewmodel.ShipmentManagerViewModel
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipientAddEditScreen(
    navController: NavHostController,
    viewModel: ShipmentManagerViewModel,
    // Este parámetro es el que recibe el objeto deserializado del NavGraph
    originalRecipient: Destinatario?
) {
    //Campos de estado inicializados con el objeto existente o vacíos si es nuevo
    var id by remember { mutableStateOf(originalRecipient?.id ?: "") }
    var nombre by remember { mutableStateOf(originalRecipient?.nombre ?: "") }
    var estado by remember { mutableStateOf(originalRecipient?.estado ?: "") }
    var codigoPostal by remember { mutableStateOf(originalRecipient?.codigoPostal ?: "") }
    var ciudad by remember { mutableStateOf(originalRecipient?.ciudad ?: "") }
    var direccion by remember { mutableStateOf(originalRecipient?.direccion ?: "") }
    var claveDestinatario by remember { mutableStateOf(originalRecipient?.claveDestinatario ?: "") }
    var telefono by remember { mutableStateOf(originalRecipient?.telefono ?: "") }

    val isEditing = originalRecipient != null
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (isEditing) "Editar Destinatario" else "Agregar Destinatario") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // asegurando que cada uno actualice su variable de estado: nombre, estado, codigoPostal, ciudad, direccion)

            OutlinedTextField(
                value = claveDestinatario,
                onValueChange = { newValue ->
                    // Validar: Solo alfanumérico y máximo 5 dígitos
                    if (newValue.length <= 5 && newValue.all { it.isLetterOrDigit() }) {
                        claveDestinatario = newValue.uppercase() // Guardar en mayúsculas
                    }
                },
                label = { Text("Clave de Destinatario (5 dígitos)") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = isEditing // No permitir cambiar la clave si se está editando
            )
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = codigoPostal, onValueChange = { codigoPostal = it }, label = { Text("Código Postal") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = ciudad, onValueChange = { ciudad = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección (Calle)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = telefono,
                onValueChange = { if (it.length <= 15) telefono = it.filter { char -> char.isDigit() } },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val recipientToSave = Destinatario(
                        id = id,
                        claveDestinatario = claveDestinatario,
                        nombre = nombre,
                        estado = estado,
                        telefono = telefono,
                        codigoPostal = codigoPostal,
                        ciudad = ciudad,
                        direccion = direccion
                    )
                    if (claveDestinatario.length == 5) {
                        // Solo guardar si la clave es válida
                        viewModel.saveRecipient(recipientToSave)
                        navController.popBackStack()
                    } else {
                        // Mostrar mensaje de error si no es válida
                        viewModel.sendMessage("La clave debe ser de 5 caracteres alfanuméricos.")
                                    }
                },
                enabled = nombre.isNotBlank() && estado.isNotBlank() && codigoPostal.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(if (isEditing) "Guardar Cambios" else "Agregar Destinatario")
            }
        }
    }
}