package com.example.apitest2.domain.api

import com.example.apitest2.domain.models.Movie
import com.example.apitest2.util.Resource

interface MoviesRepository {
    fun searchMoviesRep(expression: String) : Resource<List<Movie>>
}