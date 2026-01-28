package com.itspr.micro.screens

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.webkit.WebView
import android.print.PrintDocumentAdapter

// Clase wrapper que usa WebView para manejar la impresión de HTML
class WebViewPrintDocumentAdapter(
    context: Context,
    htmlContent: String,
    jobName: String
) : PrintDocumentAdapter() {

    private val webView: WebView = WebView(context).apply {
        // Necesitas que la WebView sea lo suficientemente grande para renderizar el contenido
        layout(0, 0, 1000, 1000)

        // Cargar el HTML que queremos imprimir
        loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    private val adapter: PrintDocumentAdapter by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.createPrintDocumentAdapter(jobName)
        } else {
            webView.createPrintDocumentAdapter(jobName)
        }
    }

    // Delegar todas las llamadas al adaptador nativo de WebView
    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: Bundle?
    ) {
        adapter.onLayout(oldAttributes, newAttributes, cancellationSignal, callback, extras)
    }

    override fun onWrite(
        pages: Array<out PageRange>?,
        destination: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback?
    ) {
        adapter.onWrite(pages, destination, cancellationSignal, callback)
    }
}