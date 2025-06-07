package com.example.apitest2.ui.movies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apitest2.Creator
import com.example.apitest2.ui.poster.PosterActivity
import com.example.apitest2.R

import com.example.apitest2.domain.api.MoviesInteractor
import com.example.apitest2.domain.models.Movie


class MainActivity : Activity() {

    // А теперь научим MoviesActivity использовать новый контроллер:

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

    private val moviesSearchController = Creator.provideMoviesSearchController(this, adapter)

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        moviesSearchController.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        moviesSearchController.onDestroy()
    }


    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}

/*

Так теперь выглядит MoviesActivity:
- Перед методом onCreate() мы объявили и проинициализировали объект moviesSearchController, передав в метод инициализации экземпляр самой активити и адаптера.
- В методах onCreate() и onDestroy() вызвали соответствующие методы контроллера.
- В самой MoviesActivity осталась только логика перехода на экран с постером и обработка событий нажатия на элемент списка через debounce.

 */