package com.example.projecto_peli.ViewModel

import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecto_peli.model.Movie
import com.example.projecto_peli.repository.MovieRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    var currentMovie = mutableStateOf<Movie?>(null)
    var title = mutableStateOf("")
    var synopsis = mutableStateOf("")
    var rating = mutableStateOf(3.0f)
    var review = mutableStateOf("")
    var imageUri = mutableStateOf<Uri?>(null)
        private set

    fun updateImageUri(uri: Uri?) {
        imageUri.value = uri
    }

    fun setMovie(movie: Movie?) {
        currentMovie.value = movie
        if (movie != null) {
            title.value = movie.title
            synopsis.value = movie.synopsis
            rating.value = movie.rating
            review.value = movie.review
            imageUri.value = if (movie.imageUri.isNotEmpty()) Uri.parse(movie.imageUri) else null
        } else {
            clearForm()
        }
    }

    fun clearForm() {
        title.value = ""
        synopsis.value = ""
        rating.value = 3.0f
        review.value = ""
        imageUri.value = null
        currentMovie.value = null
    }

    fun saveMovie() {
        viewModelScope.launch {
            val movie = currentMovie.value?.copy(
                title = title.value,
                synopsis = synopsis.value,
                rating = rating.value,
                review = review.value,
                imageUri = imageUri.value?.toString() ?: ""
            ) ?: Movie(
                title = title.value,
                synopsis = synopsis.value,
                rating = rating.value,
                review = review.value,
                imageUri = imageUri.value?.toString() ?: ""
            )

            if (currentMovie.value == null) repository.insert(movie)
            else repository.update(movie)

            clearForm()
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch { repository.delete(movie) }
    }

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> get() = _searchQuery

    private val _sortByRating = mutableStateOf(false)
    val sortByRating: State<Boolean> get() = _sortByRating

    private val _sortDescending = mutableStateOf(true)
    val sortDescending: State<Boolean> get() = _sortDescending

    val movies: StateFlow<List<Movie>> = combine(
        repository.allMovies,
        snapshotFlow { _searchQuery.value },
        snapshotFlow { _sortByRating.value },
        snapshotFlow { _sortDescending.value }
    ) { allMovies, query, byRating, descending ->
        var filtered = allMovies
        if (query.isNotBlank()) filtered = filtered.filter { it.title.contains(query, ignoreCase = true) }
        filtered = when {
            byRating -> if (descending) filtered.sortedByDescending { it.rating } else filtered.sortedBy { it.rating }
            else -> filtered.sortedBy { it.title }
        }
        filtered
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun updateSearchQuery(query: String) { _searchQuery.value = query }
    fun toggleSortByRating() { _sortByRating.value = !_sortByRating.value }
    fun toggleSortDirection() { _sortDescending.value = !_sortDescending.value }
}

