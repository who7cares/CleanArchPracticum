package com.example.apitest2.data

import android.util.Log
import com.example.apitest2.data.dto.MovieSearchRequest
import com.example.apitest2.data.dto.MoviesSearchResponse
import com.example.apitest2.domain.api.MoviesRepository
import com.example.apitest2.domain.models.Movie

private const val TAG = "MoviesRepositoryImpl"

class MoviesRepositoryImpl(private val networkClient: NetworkClient) : MoviesRepository {

    override fun searchMoviesRep(expression: String): List<Movie> {

        Log.d(TAG, "Вызван метод searchMoviesRep со значением expression = $expression")

        val response = networkClient.doRequest(MovieSearchRequest(expression))
        Log.d(TAG, "в этот импл в поле класса был передан networkClient: $networkClient. Выполнена функция doRequest и в итоге получен response = $response")

        return if (response.resultCode == 200) {
            Log.d(TAG, "Маппинг респонса")
            (response as MoviesSearchResponse).results.map {
                Movie( it.id, it.resultType, it.image, it.title, it.description)
            }
        } else {
            emptyList()
        }
    }
}