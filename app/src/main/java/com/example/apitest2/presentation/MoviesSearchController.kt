package com.example.apitest2.presentation

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apitest2.util.Creator
import com.example.apitest2.R
import com.example.apitest2.domain.api.MoviesInteractor
import com.example.apitest2.domain.models.Movie
import com.example.apitest2.ui.movies.MoviesAdapter


class MoviesSearchController(
    private val activity: Activity,
    private val adapter: MoviesAdapter
) {

    private val moviesInteractor = Creator.provideMoviesInteractor(activity)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val movies = ArrayList<Movie>()

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { searchRequest() }

    fun onCreate() {
        placeholderMessage = activity.findViewById(R.id.placeholderMessage)
        queryInput = activity.findViewById(R.id.queryInput)
        moviesList = activity.findViewById(R.id.movies)
        progressBar = activity.findViewById(R.id.progressBar)

        adapter.movies = movies

        moviesList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter

        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest() {
        if (queryInput.text.isNotEmpty()) {

            placeholderMessage.visibility = View.GONE
            moviesList.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            moviesInteractor.searchMoviesInt(queryInput.text.toString(), object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                    handler.post {
                        progressBar.visibility = View.GONE

                        if (foundMovies != null) {
                            movies.clear()
                            movies.addAll(foundMovies)
                            adapter.notifyDataSetChanged()
                            moviesList.visibility = View.VISIBLE
                        }

                        if (errorMessage != null) {
                            showMessage("Что-то пошло не так", errorMessage)
                        } else if (movies.isEmpty()) {
                            showMessage("Ничего не найдено", "")
                        } else {
                            hideMessage()
                        }
                    }
                }

            })
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            movies.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(activity, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }

    private fun hideMessage() {
        placeholderMessage.visibility = View.GONE
    }
}
