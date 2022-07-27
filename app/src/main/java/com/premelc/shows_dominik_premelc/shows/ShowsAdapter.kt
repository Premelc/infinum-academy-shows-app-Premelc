package com.premelc.shows_dominik_premelc.shows

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.ViewShowItemBinding
import com.premelc.shows_dominik_premelc.model.Show

class ShowsAdapter(
    private var items: List<Show>,
    private val handleClick: (id: String) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding =
            ViewShowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowViewHolder(binding)
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ShowViewHolder(private val binding: ViewShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Show) {
            binding.showName.text = item.name
            binding.showDescription.text = item.description
            Glide.with(binding.root.context)
                .load(
                    item.imageUrl
                ).error(
                    R.mipmap.the_office
                )
                .into(
                    binding.showImage
                )
            binding.cardContainer.setOnClickListener { handleClick(item.id) }
        }
    }

    fun addAllShows(shows: List<Show>) {
        items = shows
        notifyDataSetChanged()
    }


}