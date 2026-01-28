package com.itspr.micro.model

import com.google.firebase.firestore.DocumentId

// --- MODELO ENVÍO ---
data class Envio(
    @DocumentId
    var id: String = "",

    // Campo que relaciona con el Destinatario
    val claveDestinatario: String = "",

    // Campos requeridos por el usuario
    val descripcionProducto: String = "",
    val fechaEnvio: String = "", // Se puede cambiar a un tipo Date/Timestamp si es necesario
    val nombreDestinatario: String? = null, // ¡Asegúrate de que este campo exista
    val tipoEnvio: String = "Estándar"   //  Día siguiente, Express, Estándar

){
    // Para mostrar bonito en la impresión
    fun tipoEnvioFormateado(): String = when (tipoEnvio) {
        "DiaSiguiente" -> "Día siguiente"
        "Express" -> "Express"
        else -> "Estándar"
    }
}
