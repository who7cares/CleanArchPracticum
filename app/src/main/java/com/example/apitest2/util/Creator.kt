package com.example.apitest2.util

import android.app.Activity
import android.content.Context
import com.example.apitest2.data.MoviesRepositoryImpl
import com.example.apitest2.data.network.RetrofitNetworkClient
import com.example.apitest2.domain.api.MoviesInteractor
import com.example.apitest2.domain.api.MoviesRepository
import com.example.apitest2.domain.impl.MoviesInteractorImpl
import com.example.apitest2.presentation.movies.MoviesSearchPresenter
import com.example.apitest2.presentation.PosterController
import com.example.apitest2.presentation.movies.MoviesView
import com.example.apitest2.ui.movies.MoviesAdapter


object Creator {


    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }

    fun provideMoviesSearchPresenter(moviesView: MoviesView, context: Context): MoviesSearchPresenter {
        return MoviesSearchPresenter(
            moviesView, context)
    }

    fun providePosterController(activity: Activity): PosterController {
        return PosterController(activity)
    }


}