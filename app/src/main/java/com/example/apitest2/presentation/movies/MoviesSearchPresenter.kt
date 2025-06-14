package com.example.apitest2.presentation.movies

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.apitest2.util.Creator
import com.example.apitest2.domain.api.MoviesInteractor
import com.example.apitest2.domain.models.Movie
import com.example.apitest2.ui.models.MoviesState


class MoviesSearchPresenter(
    context: Context
) {

    private var state: MoviesState? = null
    private var view: MoviesView? = null

    private var latestSearchText: String? = null

    fun attachView(view: MoviesView) {
        this.view = view
        state?.let { view.render(it) }
    }

    fun detachView() {
        this.view = null
    }

    private fun renderState(state: MoviesState) {
        this.state = state
        this.view?.render(state)
    }

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

        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText


        lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {

            view?.render(
               MoviesState.Loading
            )

            moviesInteractor.searchMoviesInt(
                newSearchText,
                object : MoviesInteractor.MoviesConsumer {
                    override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                        handler.post {
                            val movies = mutableListOf<Movie>()
                            if (foundMovies != null) {
                                movies.clear()
                                movies.addAll(foundMovies)
                            }


                            when {
                                errorMessage != null -> {
                                    renderState(
                                        MoviesState.Error(
                                            errorMessage = "Что то пошло не так"
                                        )
                                    )
                                    view?.showToast(errorMessage)
                                }

                                movies.isEmpty() -> {
                                    renderState(
                                        MoviesState.Empty(
                                            message = "Ничего не найдено"
                                        )
                                    )
                                }

                                else -> {
                                    renderState(
                                        MoviesState.Content(
                                            movies = movies
                                        )
                                    )

                                    view?.showToast("Вот тебе список фильмов амиго")
                                }
                            }


                        }
                    }

                })
        }
    }

}
