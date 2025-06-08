package com.example.apitest2.presentation.movies

import com.example.apitest2.domain.models.Movie

interface MoviesView {

    fun showPlaceholderMessage(isVisible: Boolean)

    fun showMoviesList(isVisible: Boolean)

    fun showProgressBar(isVisible: Boolean)

    fun changePlaceholderText(newPlaceholderText: String)

    fun updateMoviesList(newMoviesList: List<Movie>)

    fun showToast(additionalMessage: String)

}