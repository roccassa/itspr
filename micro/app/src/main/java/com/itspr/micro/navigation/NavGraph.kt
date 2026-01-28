package com.itspr.micro.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.itspr.micro.model.Destinatario
import com.itspr.micro.model.Envio
import com.itspr.micro.screens.*
import com.itspr.micro.viewmodel.*
import java.net.URLDecoder


@Composable
fun NavGraph(
    authViewModel: AuthViewModel,
    zipCodeViewModel: ZipCodeViewModel,
    shipmentManagerViewModel: ShipmentManagerViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") { loginScreen(navController, authViewModel) }
        composable("signup") { signupScreen(navController, authViewModel) }
        composable("home") { homeScreen(navController, authViewModel) }
        composable("forgotPassword") { ForgotPasswordScreen(navController, authViewModel) }
        composable("profile") { ProfileScreen(navController, authViewModel) }
        composable("zipCode") { ZipCodeSearchScreen(zipCodeViewModel, navController) }

        composable("shipment_crud_home") { ShipmentHomeScreen(navController) }

        // 1. Ver/Lista de Destinatarios
        composable("recipient_list") { RecipientListScreen(navController = navController, viewModel = shipmentManagerViewModel) }

        // 2. Agregar/Editar Destinatario (Ruta con Argumento)
        composable("recipient_add") {
            RecipientAddEditScreen(
                navController = navController,
                viewModel = shipmentManagerViewModel,
                originalRecipient = null
            )
        }

        composable(
            route = "recipient_edit/{recipientJson}",
            arguments = listOf(navArgument("recipientJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipientJson = backStackEntry.arguments!!.getString("recipientJson")!!
            val decodedJson = URLDecoder.decode(recipientJson, "UTF-8")
            val recipient = Gson().fromJson(decodedJson, Destinatario::class.java)
            RecipientAddEditScreen(
                navController = navController,
                viewModel = shipmentManagerViewModel,
                originalRecipient = recipient
            )
        }


        // 1. Ver/Lista de Envíos
        composable("shipment_list") {
            ShipmentListScreen(
                navController = navController,
                viewModel = shipmentManagerViewModel
            )
        }

        // Ruta para AGREGAR un envío nuevo (sin argumentos)
        composable("shipment_add") {
            ShipmentAddEditScreen(
                navController = navController,
                viewModel = shipmentManagerViewModel,
                originalEnvio = null
            )
        }

        // Ruta para EDITAR un envío existente
        composable(
            route = "shipment_edit/{shipmentJson}",
            arguments = listOf(navArgument("shipmentJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val shipmentJson = backStackEntry.arguments!!.getString("shipmentJson")!!
            val decodedJson = URLDecoder.decode(shipmentJson, "UTF-8")
            val envio = Gson().fromJson(decodedJson, Envio::class.java)

            ShipmentAddEditScreen(
                navController = navController,
                viewModel = shipmentManagerViewModel,
                originalEnvio = envio
            )
        }

        composable(
            route = "shipment_print/{shipmentJson}",
            arguments = listOf(navArgument("shipmentJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("shipmentJson")
            val shipment: Envio? = if (json != null && json != "null") {
                // Deserializa el objeto Envio
                Gson().fromJson(URLDecoder.decode(json, "UTF8"), Envio::class.java)
            } else {
                null
            }

            if (shipment != null) {
                ShipmentPrintScreen(
                    navController = navController,
                    viewModel = viewModel(),
                    shipmentToPrint = shipment
                )
            } else {
                // Manejar error de deserialización (opcional)
                Text("Error: No se pudo cargar el documento de envío.")
            }
        }

    }
}