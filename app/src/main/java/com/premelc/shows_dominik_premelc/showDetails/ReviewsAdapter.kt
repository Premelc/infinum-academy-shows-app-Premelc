package com.premelc.shows_dominik_premelc.showDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
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
                if(item.comment != null && item.comment.isNotEmpty())reviewMsg.text = item.comment
                else reviewMsg.isVisible = false
                username.text = item.user.email.substringBefore('@')
                gradeValue.text = item.rating.toString()
                binding.profilePhoto.setDimensions(binding.root.context.resources.getDimensionPixelSize(R.dimen.reviewProfilePicture))
                binding.profilePhoto.setPicture(item.user.image_url.toString())
            }
        }
    }

    fun addAllReviews(reviews: List<Review>) {
        items = reviews
        notifyDataSetChanged()
    }

}