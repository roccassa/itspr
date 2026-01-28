package com.itspr.micro.model

import com.google.firebase.firestore.DocumentId

// --- MODELO DESTINATARIO ---
data class Destinatario(
    // ID de Firebase generado automáticamente
    @DocumentId
    var id: String = "",

    // Campos requeridos por el usuario
    val nombre: String = "",
    val estado: String = "", // Estado (Ciudad)
    val codigoPostal: String = "",
    val ciudad: String = "",
    val direccion: String = "", // Calle / Dirección
    val telefono: String = "",
    val claveDestinatario: String = "",
)