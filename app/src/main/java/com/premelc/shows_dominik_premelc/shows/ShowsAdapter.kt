package com.premelc.shows_dominik_premelc.shows

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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

    fun isEmpty() = items.isEmpty()

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ShowViewHolder(private val binding: ViewShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Show) {
            binding.showName.text = item.title
            binding.showDescription.text = item.description
            Glide.with(binding.root.context)
                .load(
                    item.image_url
                )
                .placeholder(
                    R.mipmap.ic_launcher
                )
                .error(
                    R.mipmap.ic_launcher
                )
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
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