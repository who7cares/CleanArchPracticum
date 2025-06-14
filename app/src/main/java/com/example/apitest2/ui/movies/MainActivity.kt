package com.example.apitest2.ui.movies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.example.apitest2.MoviesApplication
import com.example.apitest2.util.Creator
import com.example.apitest2.ui.poster.PosterActivity
import com.example.apitest2.R
import com.example.apitest2.domain.models.Movie
import com.example.apitest2.presentation.movies.MoviesSearchPresenter
import com.example.apitest2.presentation.movies.MoviesView
import com.example.apitest2.ui.models.MoviesState


class MainActivity : Activity(), MoviesView {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val adapter = MoviesAdapter{
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.image)
            startActivity(intent)
        }
    }

    private var moviesSearchPresenter: MoviesSearchPresenter? = null

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var textWatcher: TextWatcher? = null

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())


    override fun onStart() {
        super.onStart()
        moviesSearchPresenter?.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        moviesSearchPresenter?.attachView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moviesSearchPresenter = (this.application as? MoviesApplication)?.moviesSearchPresenter

        if(moviesSearchPresenter == null) {
            moviesSearchPresenter = Creator.provideMoviesSearchPresenter(
                context = this.applicationContext
            )
            (this.applicationContext as? MoviesApplication)?.moviesSearchPresenter = moviesSearchPresenter
        }


        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.queryInput)
        moviesList = findViewById(R.id.movies)
        progressBar = findViewById(R.id.progressBar)

        moviesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter


        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                moviesSearchPresenter!!.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        textWatcher?.let { queryInput.addTextChangedListener(it) }


    }


    override fun onPause() {
        super.onPause()
        moviesSearchPresenter?.detachView()
    }

    override fun onStop() {
        super.onStop()
        moviesSearchPresenter?.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        moviesSearchPresenter?.detachView()
    }

    override fun onDestroy() {
        super.onDestroy()
        moviesSearchPresenter?.detachView()
        textWatcher?.let { queryInput.removeTextChangedListener(it) }
        moviesSearchPresenter?.onDestroy()

        if (isFinishing()) {
            // Очищаем ссылку на Presenter в Application
            (this.application as? MoviesApplication)?.moviesSearchPresenter = null
        }
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun showLoading() {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showError(erorrMessage: String) {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholderMessage.text = erorrMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(movies: List<Movie>) {
        moviesList.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE

        adapter.movies.clear()
        adapter.movies.addAll(movies)
        adapter.notifyDataSetChanged()
    }

    override fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Empty -> showEmpty(state.message)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Loading -> showLoading()
        }
    }


    override fun showToast(additionalMessage: String) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG)
            .show()
    }





}

