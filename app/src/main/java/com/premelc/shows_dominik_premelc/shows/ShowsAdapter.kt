package com.premelc.shows_dominik_premelc.shows

import CustomOnClickHandler
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.premelc.shows_dominik_premelc.databinding.ViewShowItemBinding
import com.premelc.shows_dominik_premelc.model.Show
import com.premelc.shows_dominik_premelc.shows.ShowDetailsActivity.Companion.buildShowDetailsActivityIntent
import java.io.Serializable

class ShowsAdapter(
    private var items: List<Show>,
    private val username: String = "MISSING_USERNAME",
    private val handleClick: (View, String, String) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding = ViewShowItemBinding.inflate(LayoutInflater.from(parent.context))
        return ShowViewHolder(binding)
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addAllShows(shows: List<Show>) {
        items = shows
        notifyDataSetChanged()
    }

    inner class ShowViewHolder(private val binding: ViewShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Show) {
            binding.showName.text = item.name
            binding.showDescription.text = item.description
            binding.showImage.setImageResource(item.imageResourceId)
            binding.cardContainer.setOnClickListener { handleClick(it, item.id, username) }
        }
    }
}