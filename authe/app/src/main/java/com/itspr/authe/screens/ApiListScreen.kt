package com.itspr.authe.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.itspr.authe.model.*
//import com.itspr.authe.viewmodel.CharacterViewModel
import com.itspr.authe.viewmodel.ProductViewModel
import java.net.URLEncoder
import kotlin.text.category

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(viewModel:CharacterViewModel , navController: NavHostController) {
    val character = viewModel.character.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Harry Potter Characters") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(character.value) { character ->
                CharacterItem(character ){
                    val characterJson = URLEncoder.encode(Gson().toJson(character), "UTF8")
                    navController.navigate("detail/$characterJson")
                }

            }
        }
    }
}


@Composable
fun CharacterItem(character: Character, onclick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{onclick()},
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = character.image),
                contentDescription = character.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
            )
            Column {
                Text(text = character.name, style =
                    MaterialTheme.typography.titleLarge)
                Text(text = "Gender: ${character.gender}")
               // Text(text = "House: ${character.house}")

            }
        }
    }
}
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(viewModel: ProductViewModel, navController: NavHostController) {
    val products = viewModel.products.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("FakeStore Products") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(products.value) { product ->
                ProductItem(product) {
                    val productJson = URLEncoder.encode(Gson().toJson(product), "UTF8")
                    navController.navigate("detail/$productJson")

                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onclick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onclick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(model = product.image),
                contentDescription = product.title,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
            )
            Column {
                Text(text = product.title, style = MaterialTheme.typography.titleLarge)
                Text(text = "Price: $${product.price}")
            }
        }
    }
}

