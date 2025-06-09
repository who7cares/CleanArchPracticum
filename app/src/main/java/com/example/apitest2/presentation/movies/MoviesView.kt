package com.example.apitest2.presentation.movies

import com.example.apitest2.domain.models.Movie
import com.example.apitest2.ui.models.MoviesState

interface MoviesView {

    // Методы, меняющие внешний вид экрана


    fun render(state: MoviesState)


    // Методы одноразовых событий»

    fun showToast(additionalMessage: String)

}