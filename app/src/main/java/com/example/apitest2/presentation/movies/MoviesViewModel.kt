package com.example.apitest2.presentation.movies

import android.content.Context
import android.icu.text.SearchIterator
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.apitest2.MoviesApplication
import com.example.apitest2.domain.api.MoviesInteractor
import com.example.apitest2.domain.api.SearchHistoryInteractor
import com.example.apitest2.domain.models.Movie
import com.example.apitest2.ui.models.MoviesState
import com.example.apitest2.util.Creator

class MoviesViewModel(context: Context): ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as MoviesApplication )
                MoviesViewModel(app)
            }
        }

    }

    private val stateLiveData = MutableLiveData<MoviesState>()
    fun observeState(): LiveData<MoviesState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    private val historyMovies = MutableLiveData<List<Movie>>()
    fun observeHistoryMovies(): LiveData<List<Movie>> = historyMovies


    private val moviesInteractor = Creator.provideMoviesInteractor(context)
    private val historyMoviesInteractor = Creator.provideSearchHistoryInteractor(context)

    private var lastSearchText: String = ""

    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {

        if (lastSearchText == changedText) {
            return
        }

        this.lastSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )

    }

     fun loadHistory() {
        historyMoviesInteractor.getHistory(
            object : SearchHistoryInteractor.HistoryConsumer {
                override fun consume(searchHistory: List<Movie>?) {
                    historyMovies.postValue(searchHistory ?: emptyList())
                }
            }
        )
    }

     fun saveToHistory(movie: Movie){
        historyMoviesInteractor.saveToHistory(movie)

    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                MoviesState.Loading
            )

            moviesInteractor.searchMoviesInt(
                newSearchText, object : MoviesInteractor.MoviesConsumer {
                    override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                        handler.post {
                            // Готовим список найденных фильмов для передачи в конструктор MoviesState

                            val movies = mutableListOf<Movie>()
                            if (foundMovies != null) {
                                movies.addAll(foundMovies)
                            }

                            when {
                                errorMessage != null -> {
                                    renderState(
                                        MoviesState.Error(
                                            errorMessage = "Что-то пошло не так"
                                        )
                                    )

                                    showToast.postValue(errorMessage)
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

                                    showToast.postValue("Вот список фильмов, амиго!")
                                }
                            }
                        }
                    }

                }
            )
        }
    }

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)

    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

}