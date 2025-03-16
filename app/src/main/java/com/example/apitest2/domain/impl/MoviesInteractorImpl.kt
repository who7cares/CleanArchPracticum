package com.example.apitest2.domain.impl

import com.example.apitest2.domain.api.MoviesInteractor
import com.example.apitest2.domain.api.MoviesRepository
import java.util.concurrent.Executors

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMovies(espression: String, consumer: MoviesInteractor.MoviesConsumer) {
        executor.execute {
            consumer.consume(repository.searchMovies(espression))
        }
    }
}