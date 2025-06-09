package com.example.apitest2.presentation.poster

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.apitest2.R

class PosterPresenter(
    private val view: PosterView,
    private val imgUrl: String
) {

    fun onCreate() {
        view.setupPosterImage(imgUrl)
    }
}