package com.example.apitest2

import android.util.Log
import com.example.apitest2.data.MoviesRepositoryImpl
import com.example.apitest2.data.network.RetrofitNetworkClient
import com.example.apitest2.domain.api.MoviesInteractor
import com.example.apitest2.domain.api.MoviesRepository
import com.example.apitest2.domain.impl.MoviesInteractorImpl

private const val TAG = "object Creator"

object Creator {


    private fun getMoviesRepository(): MoviesRepository {
        val b =  MoviesRepositoryImpl(RetrofitNetworkClient())
        Log.d(TAG, "Создан объект MoviesRepository - $b")
        return b
    }

    fun provideMoviesInteractor(): MoviesInteractor {
        val a = MoviesInteractorImpl(getMoviesRepository())
        Log.d(TAG, "Создан объект MoviesInteractor - $a")
        return a
    }


}