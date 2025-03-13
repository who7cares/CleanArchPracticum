package com.example.apitest2

import android.app.Activity
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : Activity() {

    private val imdbBaseUrl = "https://tv-api.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val imdbService = retrofit.create(IMDbApi::class.java)

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView

    private lateinit var progressBar: ProgressBar
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest() }


    private val movies = ArrayList<Movie>()

    private val adapter = MoviesAdapter{
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.image)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        placeholderMessage = findViewById(R.id.placeholderMessage)

        queryInput = findViewById(R.id.queryInput)
        moviesList = findViewById(R.id.movies)
        progressBar = findViewById(R.id.progressBar)

        adapter.movies = movies

        moviesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            movies.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }



    private fun searchRequest() {
        if (queryInput.text.isNotEmpty()) {
            moviesList.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            moviesList.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            imdbService.findMovie(queryInput.text.toString()).enqueue(object :
                Callback<MoviesResponse> {
                override fun onResponse(call: Call<MoviesResponse>,
                                        response: Response<MoviesResponse>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.code() == 200) {
                        movies.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            moviesList.visibility = View.VISIBLE
                            movies.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (movies.isEmpty()) {
                            showMessage("ничего не найдено", "")
                        } else {
                            placeholderMessage.visibility = View.GONE
                        }
                    } else {
                        showMessage("что-то пошло не так", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    showMessage("что-то пошло не так", t.message.toString())
                }

            })
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

}