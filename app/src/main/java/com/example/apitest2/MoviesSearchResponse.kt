package com.example.apitest2

class MoviesResponse(val searchType: String,
                     val expression: String,
                     val results: List<Movie>)  {
}