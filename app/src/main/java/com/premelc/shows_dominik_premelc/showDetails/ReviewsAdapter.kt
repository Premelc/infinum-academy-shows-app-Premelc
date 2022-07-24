package com.premelc.shows_dominik_premelc.showDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.premelc.shows_dominik_premelc.FileUtil.getImageFile
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.ViewItemReviewBinding
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.shows.ShowsFragment

class ReviewsAdapter(
    private var items: List<Review>,
    private var context: Context
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
                username.text = item.username
                reviewMsg.text = item.text
                gradeValue.text = item.grade.toString()
                if (getImageFile(context, item.username) != null) Glide.with(context)
                    .load(ShowsFragment().getFileUri(getImageFile(context, item.username), context))
                    .override(52, 52).error(
                        R.mipmap.pfp
                    ).into(profilePic)
                else profilePic.setImageResource(item.profilePic)
            }
        }
    }

    fun addAllReviews(reviews: List<Review>) {
        items = reviews
        notifyDataSetChanged()
    }

}