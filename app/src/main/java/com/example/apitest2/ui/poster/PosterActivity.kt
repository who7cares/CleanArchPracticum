package com.example.apitest2.ui.poster

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.apitest2.util.Creator
import com.example.apitest2.R
import com.example.apitest2.presentation.poster.PosterPresenter
import com.example.apitest2.presentation.poster.PosterView

class PosterActivity : Activity(), PosterView {

    private lateinit var posterPresenter: PosterPresenter

    private lateinit var poster: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poster)
        poster = findViewById(R.id.poster)

        // Мы не можем создать PosterPresenter раньше,
        // потому что нам нужен imageUrl, который
        // станет доступен только после super.onCreate
        val imgUrl = intent.extras?.getString("poster", "") ?: ""

        posterPresenter = Creator.providePosterPresenter(this, imgUrl)

        posterPresenter.onCreate()


    }

    override fun setupPosterImage(url: String) {
        Glide.with(applicationContext)
            .load(url)
            .into(poster)
    }
}
