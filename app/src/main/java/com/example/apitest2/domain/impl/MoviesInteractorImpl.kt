package com.example.apitest2.domain.impl

import android.util.Log
import com.example.apitest2.domain.api.MoviesInteractor
import com.example.apitest2.domain.api.MoviesRepository
import java.util.concurrent.Executors

private const val TAG = "MoviesInteractorImpl"

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMoviesInt(expression: String, consumer: MoviesInteractor.MoviesConsumer) {
        executor.execute {

            Log.d(TAG, "В параметрах был передан repository = $repository")
            Log.d(TAG, "Функция searchMoviesInt с параметром expression = $expression запущена в отдельном потоке при помощи $executor ")

            consumer.consume(repository.searchMoviesRep(expression))
            Log.d(TAG, " было произведено consumer.consume репозитория припомощи функции searchMoviesRep")
        }
    }
}