package com.example.trailflix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions


    class ActorAdapter(private val actors: List<Actor>) :
        RecyclerView.Adapter<ActorAdapter.ActorViewHolder>() {

        class ActorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val actorImage: ImageView = view.findViewById(R.id.movie_image)
            val actorName: TextView = view.findViewById(R.id.movie_title)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.fragment_trailflix, parent, false)
            return ActorViewHolder(view)
        }

        override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
            val actor = actors[position]

            Glide.with(holder.itemView.context)
                .load("https://image.tmdb.org/t/p/w500${actor.actorImageUrl}")
                .apply(RequestOptions().transform(RoundedCorners(20)))
                .into(holder.actorImage)

            holder.actorName.text = actor.actorName ?: "Unknown"
        }

        override fun getItemCount(): Int = actors.size
    }


