package com.example.apitest2.presentation.poster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.apitest2.MoviesApplication
import com.example.apitest2.presentation.movies.MoviesViewModel

class PosterViewModel(
    private val url: String
): ViewModel() {


    companion object {

        fun getFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PosterViewModel(url)
            }
        }

    }

    private val urlLiveData = MutableLiveData<String>(url)
    fun observeUrl(): LiveData<String> = urlLiveData
}