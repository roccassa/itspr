package com.itspr.micro.viewmodel

import android.content.Context
import android.print.PrintManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.itspr.micro.model.Destinatario
import com.itspr.micro.model.Envio
import com.itspr.micro.repository.FirebaseRepository
import com.itspr.micro.screens.WebViewPrintDocumentAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ShipmentManagerViewModel(
    private val repository: FirebaseRepository = FirebaseRepository()
) : ViewModel() {

    // --- ESTADOS PARA DESTINATARIOS ---
    private val _recipients = MutableStateFlow<List<Destinatario>>(emptyList())
    val recipients: StateFlow<List<Destinatario>> = _recipients

    // --- ESTADOS PARA ENVÍOS ---
    private val _shipments = MutableStateFlow<List<Envio>>(emptyList())
    val shipments: StateFlow<List<Envio>> = _shipments

    // --- MENSAJES DE ESTADO (Carga, Error, Éxito) ---
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        loadAllData()
    }

    private fun loadAllData() {
        loadRecipients()
        loadShipments()
    }

    fun sendMessage(msg: String) {
        viewModelScope.launch {
            // Asume que _message es MutableStateFlow<String?>
            _message.emit(msg)
        }
    }

    suspend fun getRecipientByClave(clave: String): Destinatario? {
        // En lugar de buscar todos y luego filtrar, buscamos directamente por el campo

        return try {
            val querySnapshot = FirebaseFirestore.getInstance()
                .collection("destinatarios")
                .whereEqualTo("claveDestinatario", clave)
                .limit(1) // Solo necesitamos uno
                .get()
                .await()

            // Si encuentra un documento, lo convierte a Destinatario y lo devuelve
            querySnapshot.documents.firstOrNull()?.toObject(Destinatario::class.java)
        } catch (e: Exception) {
            _message.value = "Error al buscar destinatario por clave: ${e.message}"
            null
        }
    }

    // LÓGICA DESTINATARIOS

    fun loadRecipients() = viewModelScope.launch {
        _message.value = "Cargando destinatarios..."
        try {
            _recipients.value = repository.getRecipients()
            _message.value = "Destinatarios cargados."
        } catch (e: Exception) {
            _message.value = "Error al cargar destinatarios: ${e.message}"
        }
    }

    fun saveRecipient(destinatario: Destinatario) = viewModelScope.launch {
        _message.value = "Guardando destinatario..."
        try {
            repository.saveRecipient(destinatario)
            loadRecipients() // Recargar la lista
            _message.value = "Destinatario guardado con éxito."
        } catch (e: Exception) {
            _message.value = "Error al guardar: ${e.message}"
        }
    }

    fun deleteRecipient(recipientId: String) = viewModelScope.launch {
        _message.value = "Eliminando destinatario..."
        try {
            repository.deleteRecipient(recipientId)
            loadRecipients()
            _message.value = "Destinatario eliminado."
        } catch (e: Exception) {
            _message.value = "Error al eliminar: ${e.message}"
        }
    }


    // LÓGICA ENVÍOS

    fun loadShipments() = viewModelScope.launch {
        _message.value = "Cargando envíos..."
        try {
            _shipments.value = repository.getShipments()
            _message.value = "Envíos cargados."
        } catch (e: Exception) {
            _message.value = "Error al cargar envíos: ${e.message}"
        }
    }

    fun saveShipment(envio: Envio) = viewModelScope.launch {
        _message.value = "Guardando envío..."
        try {
            repository.saveShipment(envio)
            loadShipments() // Recargar la lista
            _message.value = "Envío guardado con éxito."
        } catch (e: Exception) {
            _message.value = "Error al guardar envío: ${e.message}"
        }
    }

    fun deleteShipment(shipmentId: String) = viewModelScope.launch {
        _message.value = "Eliminando envío..."
        try {
            repository.deleteShipment(shipmentId)
            loadShipments()
            _message.value = "Envío eliminado."
        } catch (e: Exception) {
            _message.value = "Error al eliminar envío: ${e.message}"
        }
    }

    fun printShipmentWithRecipient(context: Context, shipment: Envio) = viewModelScope.launch {
        // Cargar el Destinatario de forma asíncrona
        // Nota: Reemplaza esta línea con tu lógica real de búsqueda de destinatario.
        val recipient = withContext(Dispatchers.IO) {
            // SIMULACIÓN de la búsqueda (deberías usar tu función real de base de datos/API)
            getRecipientByClave(shipment.claveDestinatario)
        }

        if (recipient != null) {
            // Generar el HTML con la información combinada
            val htmlContent = generateShipmentHtml(shipment, recipient)

            // Pasar el HTML al generador de PDF (utilizando WebView)
            printHtmlAsPdf(context, shipment.id, htmlContent)
        } else {
            // Manejar error si no se encuentra el destinatario
            // Aquí podrías actualizar un estado de mensaje para mostrar un toast.
            sendMessage("Error: Destinatario no encontrado para la clave ${shipment.claveDestinatario}")
        }
    }

    // Función que genera el HTML (Basado en el diseño de tu PrintableDocumentCard)
    private fun generateShipmentHtml(shipment: Envio, recipient: Destinatario): String {
        val recipientAddress = "${recipient.direccion}, CP ${recipient.codigoPostal}"
        val recipientCityState = "${recipient.ciudad}, ${recipient.estado}"

        // Estilos básicos para la visualización del documento.
        val styles = """
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        .document-container { padding: 20px; border: 1px solid #ccc; max-width: 600px; margin: 20px auto; }
        h1 { font-size: 20px; border-bottom: 2px solid #333; padding-bottom: 5px; margin-bottom: 15px; }
        h2 { font-size: 16px; color: #555; margin-top: 20px; margin-bottom: 10px; }
        .data-row { display: flex; padding: 4px 0; border-bottom: 1px dotted #eee; }
        .label { font-weight: bold; width: 40%; color: #666; }
        .value { width: 60%; }
        .divider { height: 1px; background-color: #ccc; margin: 15px 0; }
    """.trimIndent()

        val htmlBody = """
        <div class="document-container">
            <h1>DOCUMENTO DE ENVÍO</h1>

            <h2>DATOS DEL ENVÍO</h2>
            <div class="data-row"><span class="label">ID Envío:</span><span class="value">${shipment.id}</span></div>
            <div class="data-row"><span class="label">ID Envío:</span><span class="value">${shipment.tipoEnvio}</span></div>
            <div class="data-row"><span class="label">Clave Destinatario:</span><span class="value">${shipment.claveDestinatario}</span></div>
            <div class="data-row"><span class="label">Producto:</span><span class="value">${shipment.descripcionProducto}</span></div>
            <div class="data-row"><span class="label">Fecha de Envío:</span><span class="value">${shipment.fechaEnvio}</span></div>

            <div class="divider"></div>

            <h2>DATOS DEL DESTINATARIO</h2>
            <div class="data-row"><span class="label">Nombre:</span><span class="value">${recipient.nombre}</span></div>
            <div class="data-row"><span class="label">Dirección:</span><span class="value">${recipientAddress}</span></div>
            <div class="data-row"><span class="label">Ciudad/Estado:</span><span class="value">${recipientCityState}</span></div>
            <div class="data-row"><span class="label">Teléfono:</span><span class="value">${recipient.telefono}</span></div>
        </div>
    """.trimIndent()

        return "<html><head><style>$styles</style></head><body>$htmlBody</body></html>"
    }

    // Función que usa un WebView para renderizar el HTML y generar el PDF
    private fun printHtmlAsPdf(context: Context, shipmentId: String, htmlContent: String) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val jobName = "Envio Documento ${shipmentId}"

        // Usar PrintDocumentAdapter que se basa en la vista web (WebView)
        val printAdapter = WebViewPrintDocumentAdapter(context, htmlContent, jobName)

        // Iniciar el proceso de impresión/guardado como PDF
        printManager.print(jobName, printAdapter, null)
    }


}
