package com.itspr.authe.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.itspr.authe.model.Character
import com.itspr.authe.model.Product

/*
@Composable
fun CharacterDetailScreen(character: Character){

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
    ){
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = rememberAsyncImagePainter(character.image),
            contentDescription = character.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        //HP
        Text("Name: ${character.name}", style = MaterialTheme.typography.titleLarge)
        Text("House: ${character.house}", fontSize = 18.sp)
        Text("Gender: ${character.gender}", fontSize = 18.sp)
        Text("Alive: ${character.alive}", fontSize = 18.sp)
        Text("Ancestry: ${character.ancestry}", fontSize = 18.sp)
        Text("Actor: ${character.actor}", fontSize = 18.sp)
        Text("Wizard: ${character.wizard}", fontSize = 18.sp)
        Text("Eye Color: ${character.eyeColour}", fontSize = 18.sp)
        Text("Hair Color: ${character.hairColour}", fontSize = 18.sp)
        Text("Date of Birth: ${character.dateOfBirth}", fontSize = 18.sp)
        Text("Core: ${character.wand.core}", fontSize = 18.sp)
        Text("Wood: ${character.wand.wood}", fontSize = 18.sp)
        Text(
            text = "Alternate Names: \n${character.alternate_names.joinToString(separator = "\n")}",
            fontSize = 18.sp
        )

        //RICK MORTY
        /*
        Text("Name: ${character.name}", style = MaterialTheme.typography.titleLarge)
        Text("Status: ${character.status}")
        Text("Gender: ${character.gender}")
        Text("Type: ${character.type}")
        Text("Species: ${character.species}")
        Text("Origin: ${character.origin.name}")
        Text("Name: ${character.location.name}")
        Text("Created: ${character.created}")
        Text("URL: ${character.url}")

         */
    }
}
*/
@Composable
fun ProductDetailScreen(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = rememberAsyncImagePainter(product.image),
            contentDescription = product.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Title: ${product.title}", style = MaterialTheme.typography.titleLarge)
        Text("Price: $${product.price}", fontSize = 18.sp)
        Text("Category: ${product.category}", fontSize = 18.sp)
        Text("Description: ${product.description}", fontSize = 18.sp)
        Text("Rating: ${product.rating.rate} (${product.rating.count} reviews)", fontSize = 18.sp)
    }
}



