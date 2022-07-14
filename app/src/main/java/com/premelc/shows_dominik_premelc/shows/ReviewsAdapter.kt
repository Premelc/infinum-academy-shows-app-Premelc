package com.premelc.shows_dominik_premelc.shows

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.premelc.shows_dominik_premelc.databinding.ViewItemReviewBinding
import com.premelc.shows_dominik_premelc.model.Review

class ReviewsAdapter(
    private var context: Context,
    private var items: List<Review>
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): ReviewViewHolder{
        val binding = ViewItemReviewBinding.inflate(LayoutInflater.from(parent.context))
        return ReviewViewHolder(binding)
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: ReviewViewHolder , position: Int) {
        holder.bind(items[position])
    }

    inner class ReviewViewHolder(private val binding: ViewItemReviewBinding):
            RecyclerView.ViewHolder(binding.root) {
                fun bind(item: Review){
                    binding.username.text = item.username
                    binding.reviewMsg.text = item.text
                    binding.gradeValue.text = item.grade.toString()
                    binding.profilePic.setImageResource(item.profilePic)
                }
    }

    fun getAllReviews():List<Review>{
        return items
    }

    fun addAllReviews(reviews: List<Review>) {
        items = reviews
        notifyDataSetChanged()
    }

    fun addItem(review: Review){
        items = items+review
        notifyItemInserted(items.lastIndex)
    }
}