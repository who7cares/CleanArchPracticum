package com.example.apitest2.util

import android.content.Context
import com.example.apitest2.data.MoviesRepositoryImpl
import com.example.apitest2.data.SearchHistoryRepositoryImpl
import com.example.apitest2.data.network.RetrofitNetworkClient
import com.example.apitest2.data.storage.PrefsStorageClient
import com.example.apitest2.domain.api.MoviesInteractor
import com.example.apitest2.domain.api.MoviesRepository
import com.example.apitest2.domain.api.SearchHistoryInteractor
import com.example.apitest2.domain.api.SearchHistoryRepository
import com.example.apitest2.domain.impl.MoviesInteractorImpl
import com.example.apitest2.domain.impl.SearchHistoryInteractorImpl
import com.example.apitest2.domain.models.Movie
import com.google.gson.reflect.TypeToken


object Creator {


    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            PrefsStorageClient<ArrayList<Movie>>(
            context,
            "HISTORY",
            object : TypeToken<ArrayList<Movie>>() {}.type)
        )
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

}