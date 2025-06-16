package com.example.apitest2.domain.api

import com.example.apitest2.domain.models.Movie
import com.example.apitest2.util.Resource

interface SearchHistoryRepository {
    fun saveToHistory(m: Movie)
    fun getHistory(): Resource<List<Movie>>
}