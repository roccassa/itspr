package com.itspr.authe.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.itspr.authe.viewmodel.CalculationViewModel

import com.itspr.authe.viewmodel.Calculation

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.TextButton


// Función calcular se mantiene igual
fun calcular(num1: String, num2: String, oper: String): String {
    val n1 = num1.toDoubleOrNull()
    val n2 = num2.toDoubleOrNull()

    if (n1 == null || n2 == null) return "Error de formato"

    return when (oper) {
        "+" -> (n1 + n2).toString()
        "-" -> (n1 - n2).toString()
        "*" -> (n1 * n2).toString()
        "/" -> if (n2 != 0.0) (n1 / n2).toString() else "Div/0 Error"
        else -> "Error de operación"
    }
}


@Composable
fun CalculadoraScreen(
    navController: NavController,
    viewModel: CalculationViewModel = viewModel()
) {
    //  Lista reactiva de cálculos guardados en Firestore
    val calculations by viewModel.calculations.collectAsState()

    var n1 by remember { mutableStateOf("") }
    var n2 by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf("") }
    var result1 by remember { mutableStateOf("") }
    var result2 by remember { mutableStateOf("") }

    val yellowBackground = Color(0xFFFBF4D3)
    val blueButton = Color(0xFF99A9F0)
    val redButton = Color(0xFFE88A8A)
    val lightGreyField = Color(0xFFF0F0F0)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Registro")
        Text(text = "de Operaciones")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // --- CALCULADORA ---
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            //  ITEM CORREGIDO: CAMPOS CANTIDAD 1 Y CANTIDAD 2 A LA MITAD
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre los campos
                ) {
                    // CANTIDAD 1
                    OutlinedTextField(
                        value = n1,
                        onValueChange = { n1 = it },
                        label = { Text("CANTIDAD 1", color = Color.Gray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f) //  Ocupa la mitad del espacio
                            .height(60.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    // CANTIDAD 2
                    OutlinedTextField(
                        value = n2,
                        onValueChange = { n2 = it },
                        label = { Text("CANTIDAD 2", color = Color.Gray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f) // Ocupa la otra mitad del espacio
                            .height(60.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }
            item {
                // Botones de Operación (+ - * /)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val operators = listOf("+", "-", "*", "/")
                    operators.forEach { op ->
                        Button(
                            onClick = { operation = op },
                            modifier = Modifier
                                .size(60.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (operation == op) MaterialTheme.colorScheme.secondary else blueButton,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(op, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                // RESULTADO
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            //.background(lightGreyField, RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = if (result1.isEmpty()) "RESULTADO" else result1,
                            fontSize = 18.sp
                            //color = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { result1 = "" },
                        modifier = Modifier
                            .width(100.dp)
                            .height(60.dp),
                        shape = RoundedCornerShape(8.dp),
                        //colors = ButtonDefaults.buttonColors(containerColor = redButton),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text("BORRAR", color = Color.White, fontSize = 12.sp)
                    }
                }
            }



            item {
                // Botón principal de CALCULAR Y GUARDAR
                Button(
                    onClick = {
                        val calculatedResult = calcular(n1, n2, operation)
                        result1 = calculatedResult

                        if (!calculatedResult.startsWith("Error")) {
                            viewModel.saveCalculation(n1, n2, operation, calculatedResult)
                        }

                        n1 = ""
                        n2 = ""
                        operation = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    //colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(text = "CALCULAR", fontSize = 18.sp, color = Color.White)
                }
            }



            // --- RESULTADOS DE FIRESTORE ---
            item {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "HISTORIAL",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                    //color = MaterialTheme.colorScheme.primary
                )
            }

            items(calculations) { calculation ->
                CalculationItem(calculation = calculation)

            }

            item { Spacer(modifier = Modifier.height(40.dp)) }

            item{
                Button(onClick = {
                    navController.navigate("list") {
                        popUpTo("calculadora") { inclusive = true }
                    }
                },
                    modifier = Modifier
                        .padding(bottom = 60.dp)

                ) {
                    Text(text = "API")
                }
            }

        }


    }

}

@Composable
fun CalculationItem(calculation: Calculation) {
    //val dateFormatter = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            // .background(Color.White, RoundedCornerShape(4.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${calculation.num1} ${calculation.operation} ${calculation.num2} = ",
                fontSize = 16.sp
                //color = Color.DarkGray
            )
            Text(
                text = calculation.result.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
                //color = MaterialTheme.colorScheme.primary
            )
        }



        // Timestamp
        /*
        Text(
            text = dateFormatter.format(calculation.fecha),
            fontSize = 12.sp,
            color = Color.Gray
        )
         */
    }
}