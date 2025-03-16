package com.example.apitest2.data.dto

import com.example.apitest2.domain.models.Movie

class MoviesSearchResponse(val searchType: String,
                           val expression: String,
                           val results: List<MovieDto>) : Response()  {
}
//