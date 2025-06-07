package com.example.apitest2.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.apitest2.data.NetworkClient
import com.example.apitest2.data.dto.MovieSearchRequest
import com.example.apitest2.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val imdbBaseUrl = "https://tv-api.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imdbService = retrofit.create(IMDbApi::class.java)

    override fun doRequest(dto: Any): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        if (dto is MovieSearchRequest) {
            val call = imdbService.findMovie(dto.expression) // Call<MoviesSearchResponse>
            val resp =
                call.execute() // Вот здесь происходит синхронный сетевой HTTP-запрос, Метод блокирует поток до тех пор, пока не получит ответ от сервера. resp — это объект типа Response<MoviesSearchResponse>
            val body = resp.body()
                ?: Response().apply { resultCode = resp.code() }   // извлекает тело ответа от сервера, уже десериализованное в объект MoviesSearchResponse?. В случае null - присваивается уже созданный нами класс Response
            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}