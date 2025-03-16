package com.example.apitest2.data.network

import com.example.apitest2.data.NetworkClient
import com.example.apitest2.data.dto.MovieSearchRequest
import com.example.apitest2.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitNetworkClient : NetworkClient {

    private val imdbBaseUrl = "https://tv-api.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imdbService = retrofit.create(IMDbApi::class.java)

    override fun doRequest(dto: Any): Response {
       if (dto is MovieSearchRequest) {
           val call = imdbService.findMovie(dto.expression) // Call<MoviesSearchResponse>
           val resp = call.execute() // Response<MoviesSearchResponse> - возвращает HTTP response из библиотеки ретрофита (Это НЕ созданный нами Response!!!)

           val body = resp.body() ?: Response()   // возвращает MoviesSearchResponse?. В случае null - присваивается уже созданный нами класс Response

           return body.apply { resultCode = resp.code() }
       } else {
           return Response().apply { resultCode = 400 }
       }
    }
}