package com.example.apitest2

import android.app.Application
import com.example.apitest2.presentation.movies.MoviesSearchPresenter

class MoviesApplication: Application() {

    var moviesSearchPresenter: MoviesSearchPresenter? = null

}