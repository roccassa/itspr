package com.itspr.micro.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.itspr.micro.model.Envio
import com.itspr.micro.viewmodel.ShipmentManagerViewModel
import java.net.URLEncoder
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import com.itspr.micro.R
// para imprimir
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipmentListScreen(
    navController: NavHostController,
    viewModel: ShipmentManagerViewModel
) {
    val shipments = viewModel.shipments.collectAsState().value
    val message = viewModel.message.collectAsState().value
    val context = LocalContext.current
    // Recargar la lista cada vez que se entra a la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadShipments()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Ver Envíos") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // NAVEGACIÓN PARA AGREGAR: Pasamos "null" como JSON
                navController.navigate("shipment_add")
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar envío")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            message?.let { Text(it, modifier = Modifier.padding(8.dp)) }

            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                items(shipments) { shipment ->
                    ShipmentListItem(
                        envio = shipment,
                        onEdit = {
                            val shipmentJson = URLEncoder.encode(Gson().toJson(shipment), "UTF8")
                            navController.navigate("shipment_add_edit/$shipmentJson")
                        },
                        onDelete = { viewModel.deleteShipment(shipment.id) },
                        onPrint = {
                          //  printShipmentAsPdf(context, shipment) // Navegar a la nueva ruta de impresión
                            viewModel.printShipmentWithRecipient(context, shipment)
                        }
                    )
                }
            }
        }
    }
}

fun printShipmentAsPdf(context: Context, shipment: Envio) {
    // Obtener el servicio de impresión
    val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

    // Definir el nombre del trabajo de impresión
    val jobName = "${context.getString(R.string.app_name)} - Envio ${shipment.id}"

    // Crear el adaptador de impresión (PrintDocumentAdapter)
    //  se define el contenido del PDF
    val printAdapter = object : PrintDocumentAdapter() {
        override fun onLayout(
            oldAttributes: PrintAttributes?,
            newAttributes: PrintAttributes,
            cancellationSignal: CancellationSignal?,
            callback: LayoutResultCallback?,
            extras: Bundle?
        ) {
            if (cancellationSignal?.isCanceled == true) {
                callback?.onLayoutCancelled()
                return
            }
            // Definir las propiedades del documento (tipo, número de páginas)
            val pdi = PrintDocumentInfo.Builder(jobName)
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(1) // Un solo documento
                .build()

            // Notificar que el layout ha finalizado
            callback?.onLayoutFinished(pdi, oldAttributes != newAttributes)
        }

        override fun onWrite(
            pages: Array<out PageRange>?,
            destination: ParcelFileDescriptor?,
            cancellationSignal: CancellationSignal?,
            callback: WriteResultCallback?
        ) {
            if (cancellationSignal?.isCanceled == true) {
                callback?.onWriteCancelled()
                return
            }

            // Aquí se dibuja el contenido del PDF
            try {
                // Contenido que se desea imprimir/guardar
                val content = "Detalles del Envío:\n" +
                        "ID: ${shipment.id}\n" +
                        "Destinatario: ${shipment.nombreDestinatario ?: shipment.claveDestinatario}\n" +
                        "Producto: ${shipment.descripcionProducto}\n" +
                        "Fecha: ${shipment.fechaEnvio}\n"

                // Usar PdfDocument para dibujar texto en el Canvas (método simple)
                val document = PdfDocument()
                val pageInfo = PdfDocument.PageInfo.Builder(600, 800, 1).create()
                val page = document.startPage(pageInfo)
                val canvas = page.canvas
                val paint = Paint()
                paint.textSize = 12f

                // Dibujar el texto línea por línea (ejemplo básico)
                var y = 40f
                content.split("\n").forEach { line ->
                    canvas.drawText(line, 40f, y, paint)
                    y += 20f
                }

                document.finishPage(page)

                // Escribir el documento en el archivo de destino
                document.writeTo(FileOutputStream(destination?.fileDescriptor))

                // Cerrar el documento y notificar el éxito
                document.close()
                callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
            } catch (e: Exception) {
                e.printStackTrace()
                callback?.onWriteFailed(e.toString())
            }
        }
    }

    // Iniciar el proceso de impresión
    printManager.print(jobName, printAdapter, null)
    //
}

// Item Composable (sin cambios en la estructura interna)
@Composable
fun ShipmentListItem(
    envio: Envio,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onPrint: () -> Unit
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

                val clavve = envio.claveDestinatario
                // Mostrar el nombre del destinatario (si existe)
                Text("ID de destinatario: ${clavve}", style = MaterialTheme.typography.titleMedium)
                val nombre = envio.nombreDestinatario ?: "Clave: ${envio.claveDestinatario}"
                Text("Destintario: ${nombre}", style = MaterialTheme.typography.titleMedium)

                // Mostrar la descripción del producto
                Text("Producto: ${envio.descripcionProducto}", style = MaterialTheme.typography.bodyMedium)

                // Mostrar la fecha
                Text("Fecha de Envío: ${envio.fechaEnvio}", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.width(8.dp))

            IconButton(onClick = onPrint) {
                Icon(Icons.Filled.Print, contentDescription = "Imprimir Documento")
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}


