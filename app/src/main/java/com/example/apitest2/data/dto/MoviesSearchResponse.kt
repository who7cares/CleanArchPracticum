package com.example.apitest2.data.dto

import com.example.apitest2.domain.models.Movie

class MoviesResponse(val searchType: String,
                     val expression: String,
                     val results: List<Movie>) : Response()  {
}