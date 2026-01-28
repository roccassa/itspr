package com.itspr.parcial2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.itspr.parcial2.navigation.NavGraph
import com.itspr.parcial2.screens.CharacterListScreen
import com.itspr.parcial2.viewmodel.CharacterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: CharacterViewModel = viewModel()
            NavGraph(viewModel = viewModel)
        }
    }
}