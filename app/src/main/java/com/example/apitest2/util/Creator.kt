package com.example.apitest2.util

import android.app.Activity
import android.content.Context
import com.example.apitest2.data.MoviesRepositoryImpl
import com.example.apitest2.data.network.RetrofitNetworkClient
import com.example.apitest2.domain.api.MoviesInteractor
import com.example.apitest2.domain.api.MoviesRepository
import com.example.apitest2.domain.impl.MoviesInteractorImpl


object Creator {


    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }

}