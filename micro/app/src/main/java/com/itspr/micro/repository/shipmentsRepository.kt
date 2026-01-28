package com.itspr.micro.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.itspr.micro.model.Destinatario
import com.itspr.micro.model.Envio
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()

    // Nombres de colecciones en Firestore
    private val recipientCollection = db.collection("destinatarios")
    private val shipmentCollection = db.collection("envios")

    // ------------------------------------
    // Operaciones CRUD para Destinatarios
    // ------------------------------------

    // CREATE / UPDATE
    suspend fun saveRecipient(destinatario: Destinatario) {
        if (destinatario.id.isEmpty()) {
            // CREATE (Añadir un nuevo documento con ID generado por Firebase)
            recipientCollection.add(destinatario).await()
        } else {
            // UPDATE (Usar el ID existente)
            recipientCollection.document(destinatario.id).set(destinatario).await()
        }
    }

    // READ (Obtener todos los destinatarios)
    suspend fun getRecipients(): List<Destinatario> {
        return recipientCollection.get().await().toObjects(Destinatario::class.java)
    }

    // DELETE
    suspend fun deleteRecipient(recipientId: String) {
        recipientCollection.document(recipientId).delete().await()
    }

    // ------------------------------------
    // Operaciones CRUD para Envíos
    // ------------------------------------

    // CREATE / UPDATE
    suspend fun saveShipment(envio: Envio) {
        if (envio.id.isEmpty()) {
            // CREATE
            shipmentCollection.add(envio).await()
        } else {
            // UPDATE
            shipmentCollection.document(envio.id).set(envio).await()
        }
    }

    // READ (Obtener todos los envíos)
    suspend fun getShipments(): List<Envio> {
        return shipmentCollection.get().await().toObjects(Envio::class.java)
    }

    // DELETE
    suspend fun deleteShipment(shipmentId: String) {
        shipmentCollection.document(shipmentId).delete().await()
    }
}