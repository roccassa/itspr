package com.example.projecto_peli.repository

import com.example.projecto_peli.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieRepository {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val allMovies: StateFlow<List<Movie>> get() = _movies

    private var nextId = 1

    suspend fun insert(movie: Movie) {
        val newMovie = movie.copy(id = nextId++)
        _movies.value = _movies.value + newMovie
    }

    suspend fun update(movie: Movie) {
        _movies.value = _movies.value.map { if (it.id == movie.id) movie else it }
    }

    suspend fun delete(movie: Movie) {
        _movies.value = _movies.value.filter { it.id != movie.id }
    }
}
