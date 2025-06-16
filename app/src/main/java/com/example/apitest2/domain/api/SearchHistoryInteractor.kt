package com.example.apitest2.domain.api

import com.example.apitest2.domain.models.Movie

interface SearchHistoryInteractor {

    fun getHistory(consumer: HistoryConsumer)
    fun saveToHistory(m: Movie)

    interface HistoryConsumer {
        fun consume(searchHistory: List<Movie>?)
    }
}

/*


Интерфейс имеет два ключевых метода:
getHistory() — для получения фильмов из истории поиска. Результат будет передан экземпляру класса HistoryConsumer.
saveToHistory() - сохраняет фильм в историю поиска.
В метод consume() интерфейса HistoryConsumer будет передаваться список фильмов.


 */