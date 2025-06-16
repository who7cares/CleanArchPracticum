package com.example.apitest2.ui.poster


import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.apitest2.R
import com.example.apitest2.presentation.poster.PosterViewModel

class PosterActivity : AppCompatActivity() {

    private var viewModel: PosterViewModel? = null

    private lateinit var poster: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poster)
        poster = findViewById(R.id.poster)


        val imgUrl = intent.extras?.getString("poster", "") ?: ""


        viewModel = ViewModelProvider(this, PosterViewModel.getFactory(imgUrl))
            .get(PosterViewModel::class.java)

        viewModel?.observeUrl()?.observe(this) {
            setupPosterImage(it)
        }

    }

    private fun setupPosterImage(url: String) {
        Glide.with(applicationContext)
            .load(url)
            .into(poster)
    }
}
