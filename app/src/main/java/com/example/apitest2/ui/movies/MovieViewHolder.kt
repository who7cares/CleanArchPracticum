package com.example.apitest2.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apitest2.R
import com.example.apitest2.domain.models.Movie

class MovieViewHolder(
    parent: ViewGroup,
    private val onSaveClick: (Movie) -> Unit
) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.i_movie, parent, false)
    ) {

    var cover: ImageView = itemView.findViewById(R.id.cover)
    var title: TextView = itemView.findViewById(R.id.title)
    var description: TextView = itemView.findViewById(R.id.description)

    var saveToFav: Button = itemView.findViewById(R.id.save_to_fav)

    fun bind(movie: Movie) {
        Glide.with(itemView)
            .load(movie.image)
            .into(cover)

        title.text = movie.title
        description.text = movie.description

        saveToFav.setOnClickListener {
            onSaveClick(movie)
        }
    }



}