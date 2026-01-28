package com.itspr.authe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.itspr.authe.navigation.NavGraph
import com.itspr.authe.ui.theme.AutheTheme
import com.itspr.authe.viewmodel.AuthViewModel
import com.itspr.authe.viewmodel.ProductViewModel

//import com.itspr.authe.viewmodel.CharacterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // ViewModel para productos
            val productViewModel: ProductViewModel = viewModel()

            // ViewModel para autenticación
            val authViewModel: AuthViewModel = viewModel()

            // Pasamos los viewmodels al NavGraph
            NavGraph(
                productViewModel = productViewModel,
                authViewModel = authViewModel
            )
        }
        /*
        setContent {
            val characterViewModel: CharacterViewModel = viewModel()

            val authViewModel: AuthViewModel = viewModel()

            NavGraph(
                characterViewModel = characterViewModel,
                authViewModel = authViewModel
            )
        }*/
    }
}

