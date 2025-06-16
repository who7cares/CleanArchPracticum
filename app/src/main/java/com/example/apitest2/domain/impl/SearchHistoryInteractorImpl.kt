package com.example.apitest2.domain.impl

import com.example.apitest2.domain.api.SearchHistoryInteractor
import com.example.apitest2.domain.api.SearchHistoryRepository
import com.example.apitest2.domain.models.Movie

class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : SearchHistoryInteractor {

    override fun getHistory(consumer: SearchHistoryInteractor.HistoryConsumer) {
        consumer.consume(repository.getHistory().data)
    }

    override fun saveToHistory(m: Movie) {
        repository.saveToHistory(m)
    }

}

/*
Обратите внимание, что:
В конструктор передаётся экземпляр SearchHistoryRepository;

В методе getHistory() интерактор запрашивает у репозитория
историю поиска и передаёт этот список в consumer;

saveToHistory() просто вызывает одноимённый методу у repository и
передаёт в него экземпляр класса Movie.
 */