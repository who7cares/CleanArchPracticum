package com.example.apitest2.ui.movies


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apitest2.ui.poster.PosterActivity
import com.example.apitest2.R
import com.example.apitest2.domain.models.Movie
import com.example.apitest2.presentation.movies.MoviesViewModel
import com.example.apitest2.ui.models.MoviesState


class MainActivity : AppCompatActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var viewModel: MoviesViewModel? = null

    private val adapter = MoviesAdapter(
        clickListener = {
            if (clickDebounce()) {
                val intent = Intent(this, PosterActivity::class.java)
                intent.putExtra("poster", it.image)
                startActivity(intent)
            }
        },
        onSaveClick = {
            viewModel?.saveToHistory(it)
            showToast("Фильм сохранён в избранное")
        }
    )

    private val favAdapter = MoviesAdapter(
        clickListener = {
            if (clickDebounce()) {
                val intent = Intent(this, PosterActivity::class.java)
                intent.putExtra("poster", it.image)
                startActivity(intent)
            }
        },
        onSaveClick = {
            // Здесь можно либо ничего не делать, либо повторно сохранить, если нужно
            showToast("Этот фильм уже в избранном")
        }
    )

    private lateinit var showHistoryButton: Button

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var favMovieList: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var textWatcher: TextWatcher? = null

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.queryInput)
        moviesList = findViewById(R.id.movies)
        favMovieList = findViewById(R.id.fav_movies)
        progressBar = findViewById(R.id.progressBar)
        showHistoryButton = findViewById(R.id.show_movies)


        moviesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter

        favMovieList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        favMovieList.adapter = favAdapter


        viewModel = ViewModelProvider(this, MoviesViewModel.getFactory())
            .get(MoviesViewModel::class.java)

        viewModel?.observeState()?.observe(this) {
            render(it)
        }

        viewModel?.observeShowToast()?.observe(this) {
            showToast(it)
        }

        viewModel?.observeHistoryMovies()?.observe(this) {
            showFavMovies(it)
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel?.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        textWatcher?.let { queryInput.addTextChangedListener(it) }


        showHistoryButton.setOnClickListener {
            viewModel?.loadHistory()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let {
            queryInput.removeTextChangedListener(it)
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

        favMovieList.visibility = View.GONE
    }

    private fun showError(erorrMessage: String) {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        favMovieList.visibility = View.GONE

        placeholderMessage.text = erorrMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(movies: List<Movie>) {
        moviesList.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE
        favMovieList.visibility = View.GONE

        adapter.movies.clear()
        adapter.movies.addAll(movies)
        adapter.notifyDataSetChanged()
    }

    private fun showFavMovies(historyMovies: List<Movie>) {
        favMovieList.visibility = View.VISIBLE
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE

        favAdapter.movies.clear()
        favAdapter.movies.addAll(historyMovies)
        favAdapter.notifyDataSetChanged()
    }

    private fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Empty -> showEmpty(state.message)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Loading -> showLoading()

            else -> {println(11)}
        }
    }


    private fun showToast(additionalMessage: String?) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG)
            .show()
    }


}


