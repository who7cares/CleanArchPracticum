package com.example.apitest2.presentation.movies

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.apitest2.util.Creator
import com.example.apitest2.domain.api.MoviesInteractor
import com.example.apitest2.domain.models.Movie



class MoviesSearchPresenter(
    private val view: MoviesView,
    context: Context,
) {

    private val moviesInteractor = Creator.provideMoviesInteractor(context)
    private val handler = Handler(Looper.getMainLooper())

    private var lastSearchText: String = ""

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


    private val movies = ArrayList<Movie>()

    private val searchRunnable = Runnable {
        searchRequest(lastSearchText)
    }


    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    fun searchDebounce(changedText: String) {
        lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {

            view.showLoading()

            moviesInteractor.searchMoviesInt(newSearchText, object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                    handler.post {

                        if (foundMovies != null) {
                            movies.clear()
                            movies.addAll(foundMovies)
                        }


                        when {
                            errorMessage != null -> {
                                view.showError("что-то пошло не так")
                                view.showToast(errorMessage)
                            }

                            movies.isEmpty() -> {
                                view.showEmpty("Ничего не найдено")
                            }

                            else -> {
                                view.showContent(movies)
                            }
                        }


                    }
                }

            })
        }
    }

}
