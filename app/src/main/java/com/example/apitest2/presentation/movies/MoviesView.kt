package com.example.apitest2.presentation.movies

import com.example.apitest2.domain.models.Movie

interface MoviesView {

    // Методы, меняющие внешний вид экрана

    // состояние загрузки
    fun showLoading()

    // состояние ошибки
    fun showError(erorrMessage: String)

    // состояние пустого списка
    fun showEmpty(emptyMessage: String)

    // состояние контента
    fun showContent(movies: List<Movie>)


    // Методы одноразовых событий»

    fun showToast(additionalMessage: String)

}