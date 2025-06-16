package com.example.apitest2

import android.content.Context
import android.content.SharedPreferences
import com.example.apitest2.domain.models.Movie
import com.google.gson.Gson

class SearchHistory(
    private val context: Context
) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "MOVIES_SEARCH", Context.MODE_PRIVATE
    )

    private val gson = Gson()


    private fun saveHistory(history: History) {
        prefs.edit()
            .putString("HISTORY", gson.toJson(history, History::class.java))
            .apply()
    }

    fun getHistory(): History {
        val h = prefs.getString("HISTORY", null)
        return if (h == null) {
            History()
        } else {
            gson.fromJson(h, History::class.java)
        }
    }

    fun addMovieToHistory(m: Movie) {
        val h = getHistory()
        h.movies.add(m)
        saveHistory(h)
    }

}