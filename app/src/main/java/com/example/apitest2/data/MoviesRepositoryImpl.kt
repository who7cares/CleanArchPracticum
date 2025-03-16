package com.example.apitest2.data

import com.example.apitest2.data.dto.MovieSearchRequest
import com.example.apitest2.data.dto.MoviesSearchResponse
import com.example.apitest2.domain.api.MoviesRepository
import com.example.apitest2.domain.models.Movie

class MoviesRepositoryImpl(private val networkClient: NetworkClient) : MoviesRepository {

    override fun searchMovies(expression: String): List<Movie> {
        val response = networkClient.doRequest(MovieSearchRequest(expression))
        return if (response.resultCode == 200) {
            (response as MoviesSearchResponse).results.map {
                Movie( it.id, it.resultType, it.image, it.title, it.description)
            }
        } else {
            emptyList()
        }
    }
}