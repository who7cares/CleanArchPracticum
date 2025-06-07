package com.example.apitest2.domain.impl

import android.util.Log
import com.example.apitest2.domain.api.MoviesInteractor
import com.example.apitest2.domain.api.MoviesRepository
import com.example.apitest2.util.Resource
import java.util.concurrent.Executors

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMoviesInt(expression: String, consumer: MoviesInteractor.MoviesConsumer) {
        executor.execute {
            val resource = repository.searchMoviesRep(expression)

            when(resource) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
    }
}