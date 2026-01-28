package com.itspr.authe.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date


// Modelo de datos para la lista de resultados
data class Calculation(
    val id: String = "", // Para identificar el documento en Firestore
    val num1: Double = 0.0,
    val num2: Double = 0.0,
    val operation: String = "",
    val result: Double = 0.0,
    //val fecha: Date = Date()
)

class CalculationViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val calculationsCollection = db.collection("calculations")

    // 🔥 Lista reactiva para observar los resultados en Compose
    private val _calculations = MutableStateFlow<List<Calculation>>(emptyList())
    val calculations: StateFlow<List<Calculation>> = _calculations

    init {
        // 🔥 Inicia la escucha de Firestore al inicializar el ViewModel
        fetchCalculations()
    }

    // Función para guardar el cálculo (se mantiene igual)
    fun saveCalculation(num1: String, num2: String, operation: String, result: String) {
        val n1 = num1.toDoubleOrNull() ?: return
        val n2 = num2.toDoubleOrNull() ?: return
        val res = result.toDoubleOrNull() ?: return

        val dataToSave = hashMapOf(
            "numero1" to n1,
            "numero2" to n2,
            "operacion" to operation,
            "resultado" to res,
            "fecha" to Date() // Usamos un timestamp de servidor/local
        )

        calculationsCollection
            .add(dataToSave)
            .addOnSuccessListener {
                println("Cálculo guardado con éxito: ${it.id}")
            }
            .addOnFailureListener { e ->
                println("Error al guardar cálculo: $e")
            }
    }

    // 🔥 Función para escuchar cambios en tiempo real (Listener)
    private fun fetchCalculations() {
        calculationsCollection
            // Ordena por fecha descendente para ver los más recientes arriba
            .orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    println("Error al escuchar cambios de Firestore: $e")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val calculationList = snapshot.documents.map { document ->
                        // Mapea el DocumentSnapshot a tu data class Calculation
                        Calculation(
                            id = document.id,
                            num1 = document.getDouble("numero1") ?: 0.0,
                            num2 = document.getDouble("numero2") ?: 0.0,
                            operation = document.getString("operacion") ?: "",
                            result = document.getDouble("resultado") ?: 0.0,
                            //fecha = document.getDate("fecha") ?: Date()
                        )
                    }
                    _calculations.value = calculationList
                }
            }
    }
}