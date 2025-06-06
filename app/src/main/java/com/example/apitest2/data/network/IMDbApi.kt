package com.example.apitest2.data.network

import com.example.apitest2.data.dto.MoviesSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// Он создаёт "заготовку" запроса (объект Call<T>), но сам запрос ещё не отправляется. Это как "накопитель" с данными запроса.
interface IMDbApi {
    @GET("/en/API/SearchMovie/k_zcuw1ytf/{expression}")
    fun findMovie(@Path("expression") expression: String): Call<MoviesSearchResponse>
}