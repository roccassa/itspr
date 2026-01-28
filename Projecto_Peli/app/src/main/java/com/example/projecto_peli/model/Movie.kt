package com.example.projecto_peli.model

data class Movie(
    val id: Int = 0,
    val title: String,
    val synopsis: String,
    val rating: Float,
    val review: String,
    val imageUri: String
)
