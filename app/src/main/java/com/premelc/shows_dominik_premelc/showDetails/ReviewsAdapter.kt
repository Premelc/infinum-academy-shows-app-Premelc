package com.premelc.shows_dominik_premelc.showDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.ViewItemReviewBinding
import com.premelc.shows_dominik_premelc.model.Review

class ReviewsAdapter(
    private var items: List<Review>,
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding =
            ViewItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ReviewViewHolder(private val binding: ViewItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            with(binding) {
                username.text = item.user.email.substringBefore('@')
                reviewMsg.text = item.comment
                gradeValue.text = item.rating.toString()
                Glide.with(binding.root.context)
                    .load(item.user.image_url)
                    .placeholder(
                        R.mipmap.pfp
                    )
                    .error(
                        R.mipmap.pfp
                    )
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(profilePic)
            }
        }
    }

    fun addAllReviews(reviews: List<Review>) {
        items = reviews
        notifyDataSetChanged()
    }

}