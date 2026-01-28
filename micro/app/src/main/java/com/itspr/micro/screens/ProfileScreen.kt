package com.itspr.micro.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.itspr.micro.R //
import com.itspr.micro.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class) // Soluciona el error experimental de Scaffold/TopAppBar
@Composable
fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    val userProfileState = authViewModel.userProfile.observeAsState()
    val profile = userProfileState.value
    val context = LocalContext.current

    // Estado de la UI para los campos editables
    var isEditing by remember { mutableStateOf(false) }
    // Inicialización de los estados con valores del perfil (o vacíos)
    var fullName by remember(profile) { mutableStateOf(profile?.fullName ?: "") }
    var city by remember(profile) { mutableStateOf(profile?.city ?: "") }
    var ageText by remember(profile) { mutableStateOf(profile?.age?.toString() ?: "") }
    var phone by remember(profile) { mutableStateOf(profile?.phoneNumber ?: "") }
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        authViewModel.fetchUserProfile()
    }

    // Launcher para seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // guardamos la URI si es válida.
        if (uri != null) {
            selectedImageUri = uri

            // Opcional: Mostrar un mensaje de éxito al seleccionar
            Toast.makeText(
                context,
                "Imagen seleccionada. Presiona 'Guardar Cambios'.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi Perfil") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val base64Data = profile?.photoBase64

            // --- Sección de Foto ---
            val painter = when {
                // Si hay una URI local nueva seleccionada, la mostramos inmediatamente
                selectedImageUri != null -> {
                    rememberAsyncImagePainter(model = selectedImageUri)
                }
                // Si hay datos Base64 guardados en Firestore, los mostramos
                !base64Data.isNullOrEmpty() -> {
                    rememberAsyncImagePainter(model = "data:image/jpeg;base64,$base64Data")
                }
                // Placeholder
                else -> {
                    painterResource(id = R.drawable.user)
                }
            }

            Image(
                painter = painter,
                contentDescription = "Foto de Perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )

            if (isEditing) {
                TextButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text("Cambiar Foto")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // --- Campos de Datos ---
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Nombre Completo") },
                readOnly = !isEditing,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = ageText,
                onValueChange = { ageText = it },
                label = { Text("Edad") },
                // Uso correcto del constructor y el objeto KeyboardType
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                readOnly = !isEditing,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Ciudad") },
                readOnly = !isEditing,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                readOnly = !isEditing,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Botones de Acción ---
            Button(
                onClick = {

                    val contentResolver = context.contentResolver

                    if (isEditing) {
                        // Creamos el perfil manteniendo la Base64 actual
                        val newProfile = profile?.copy(
                            fullName = fullName,
                            age = ageText.toIntOrNull(),
                            city = city,
                            phoneNumber = phone,
                            userId = profile.userId,
                            //photoBase64 = profile.photoBase64 // Mantiene la Base64 actual si no se selecciona una nueva
                        ) ?: return@Button

                        // LLAMAR A LA FUNCIÓN DE BASE64
                        authViewModel.saveProfileWithBase64(
                            contentResolver = contentResolver,
                            profile = newProfile,
                            // selectedImageUri solo es nulo si NO seleccionó una nueva foto
                            imageUri = selectedImageUri,
                            onSuccess = {
                                selectedImageUri = null  // ← limpiamos la URI temporal
                                authViewModel.fetchUserProfile()  // ← recarga el perfil con el nuevo Base64
                                Toast.makeText(context, "Perfil actualizado.", Toast.LENGTH_SHORT).show()
                                isEditing = false
                            },
                            onError = { msg ->
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            }
                        )

                    } else {
                        isEditing = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Guardar Cambios" else "Editar Perfil")
            }

            if (isEditing) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { isEditing = false }) {
                    Text("Cancelar")
                }
            }
        }
    }
}
