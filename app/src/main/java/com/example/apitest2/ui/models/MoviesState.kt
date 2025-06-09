package com.example.apitest2.ui.models

import com.example.apitest2.domain.models.Movie

sealed interface MoviesState {

    data object Loading: MoviesState

    data class Content(
        val movies: List<Movie>
    ): MoviesState

    data class Error(
        val errorMessage: String
    ) : MoviesState

    data class Empty(
        val message: String
    ) : MoviesState

}