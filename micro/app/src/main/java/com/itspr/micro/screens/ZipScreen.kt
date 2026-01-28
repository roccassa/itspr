package com.itspr.micro.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.itspr.micro.model.Place
import com.itspr.micro.viewmodel.ZipCodeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZipCodeSearchScreen(
    viewModel: ZipCodeViewModel,
    navController: NavHostController
) {
    // Estados que provienen del ViewModel
    val zipCodeData = viewModel.zipCodeData
    val errorMessage = viewModel.errorMessage
    val isLoading = viewModel.isLoading

    // Estado local para el campo de texto del CP
    var cpInput by remember { mutableStateOf("") }

    // Función de búsqueda local
    val performSearch: () -> Unit = {
        viewModel.searchZipCode(cpInput)
    }

    Scaffold(
        topBar = {
            Spacer(modifier = Modifier.height(50.dp))
            TopAppBar(title = { Text("Buscador de Códigos Postales (SEPOMEX)") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Campo de Busqueda ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = cpInput,
                    onValueChange = {
                        // Limitar a 5 caracteres (CP mexicano) y solo números
                        if (it.length <= 5 && it.all { char -> char.isDigit() }) {
                            cpInput = it
                        }
                    },
                    label = { Text("Código Postal (5 dígitos)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = { performSearch() }),
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = { performSearch() },
                    enabled = cpInput.length == 5, // Habilitado solo con 5 dígitos
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Icon(Icons.Filled.Search, contentDescription = "Buscar CP")
                }
            }

            // --- Indicador de Carga ---
            if (isLoading) {
                CircularProgressIndicator(Modifier.padding(16.dp))
            }

            // --- Mensaje de Error ---
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // --- Resultados ---
            zipCodeData?.let { data ->
                if (data.places.isNotEmpty()) {
                    Text(
                        text = "CP ${data.postCode}: ${data.country}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Lista de asentamientos (Colonias)
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(data.places) { place ->
                            PlaceItem(place)
                        }
                    }
                } else {
                    Text("No se encontraron resultados para el CP ${data.postCode}.",
                        modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

// --- Composable para un elemento de la lista (Colonia/Asentamiento) ---
@Composable
fun PlaceItem(place: Place) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Nombre de la Colonia/Asentamiento
            Text(
                text = "Asentamiento: ${place.placeName}",
                style = MaterialTheme.typography.titleMedium
            )
            // Estado y Abreviatura
            Text(
                text = "Estado: ${place.state} (${place.stateAbbreviation})",
                style = MaterialTheme.typography.bodyMedium
            )
            // Ciudad/Municipio
            Text(
                text = "Ciudad/Municipio: ${place.municipality}", //
                style = MaterialTheme.typography.bodySmall
            )
            // Coordenadas
            Text(
                text = "Lat: ${place.latitude}, Lon: ${place.longitude}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}