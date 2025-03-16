package com.example.apitest2.domain.api

import com.example.apitest2.domain.models.Movie

interface MoviesRepository {
    fun searchMovies(expression: String) : List<Movie>
}