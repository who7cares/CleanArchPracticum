package com.example.apitest2.domain.api

import com.example.apitest2.domain.models.Movie

interface MoviesInteractor {

    fun searchMoviesInt(expression: String, consumer: MoviesConsumer)

    interface MoviesConsumer {
        fun consume(foundMovies: List<Movie>)
    }
}

