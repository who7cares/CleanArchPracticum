package com.example.apitest2.data

import android.util.Log
import com.example.apitest2.data.dto.MovieSearchRequest
import com.example.apitest2.data.dto.MoviesSearchResponse
import com.example.apitest2.domain.api.MoviesRepository
import com.example.apitest2.domain.models.Movie
import com.example.apitest2.util.Resource

class MoviesRepositoryImpl(private val networkClient: NetworkClient) : MoviesRepository {

    override fun searchMoviesRep(expression: String): Resource<List<Movie>> {

        val response = networkClient.doRequest(MovieSearchRequest(expression))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                val movieResponse = response as MoviesSearchResponse
                val result = movieResponse.results

                if (result.isEmpty()) {
                    Resource.Error("ничего не найдено")
                } else {
                    Resource.Success(result.map {
                        Movie(it.id, it.resultType, it.image, it.title, it.description)
                    })
                }

            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}