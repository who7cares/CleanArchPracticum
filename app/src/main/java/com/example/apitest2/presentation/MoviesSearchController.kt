package com.example.apitest2.presentation

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.apitest2.Creator
import com.example.apitest2.domain.api.MoviesInteractor
import com.example.apitest2.domain.models.Movie

/*
Итак, мы перенесли сюда весь код, который связан с поиском фильмов и отображением результатов, в том числе работу с интерактором. Однако есть несколько проблем:
1. View-элементы не проинициализированы.
2. Не хватает ссылки на адаптер для RecyclerView. Мы оставили инициализацию этого объекта в MoviesActivity, поскольку в конструктор адаптера передаётся MovieClickListener, использующийся для навигации.
3. Полю для ввода поискового запроса надо установить TextWatcher, в котором можно вызвать метод searchDebounce().

Мы могли передать контроллеру уже проинициализированные View-элементы, но это всё ещё нарушения инверсии зависимости. Точно такое же нарушение, как если бы мы передали контроллеру ссылку на саму Activity и проинициализировали элементы интерфейса непосредственно в контроллере.
 */

class MoviesSearchController() {

    private val moviesInteractor = Creator.provideMoviesInteractor()

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
                override fun consume(foundMovies: List<Movie>) {
                    handler.post {
                        progressBar.visibility = View.GONE
                        movies.clear()
                        movies.addAll(foundMovies)
                        moviesList.visibility = View.VISIBLE
                        adapter.notifyDataSetChanged()
                        if (movies.isEmpty()) {
                            showMessage(activity.getString("Ничего не найдено", ""))
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
