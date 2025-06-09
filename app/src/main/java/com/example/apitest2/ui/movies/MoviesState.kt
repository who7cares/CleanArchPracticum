package com.example.apitest2.ui.movies

import com.example.apitest2.domain.models.Movie

data class MoviesState(
    val movies: List<Movie>,
    val isLoading: Boolean,
    val errorMessage: String?
    ) {
}