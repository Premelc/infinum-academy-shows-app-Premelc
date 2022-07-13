package com.premelc.shows_dominik_premelc.shows

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.premelc.shows_dominik_premelc.databinding.ActivityShowDetailsBinding
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.shows.ShowsActivity.Companion.buildShowsActivityIntent
import java.io.Serializable

class ShowDetailsActivity : AppCompatActivity() {

    companion object {
        fun buildShowDetailsActivityIntent(activity: Activity): Intent {
            return Intent(activity, ShowDetailsActivity::class.java)
        }
    }

    private lateinit var binding: ActivityShowDetailsBinding
    private lateinit var adapter:ReviewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBackButton()
        initDetails(
            intent.extras?.getString("name"),
            intent.extras?.getString("description"),
            intent.extras?.getInt("img"),
        )
        initReviewsRecycler(intent.extras?.getSerializable("reviews"))

    }

    private fun initReviewsRecycler(reviews: Serializable?) {
        adapter = ReviewsAdapter(this, emptyList())
        binding.reviewsRecycler.layoutManager = LinearLayoutManager(this)
        binding.reviewsRecycler.adapter = adapter
        adapter.addAllReviews(reviews as List<Review>)
        if (adapter.getItemCount() > 0) toggleEmptyState(false) else toggleEmptyState(true)

    }

    private fun initDetails(name: String?, description: String?, @DrawableRes img: Int?) {
        if (img != null) {
            binding.showImage.setImageResource(img)
        }
        binding.showTitle.text = name
        binding.showDescription.text = description
    }

    private fun initBackButton(){
        binding.backArrow.setOnClickListener(){
            val intent = buildShowsActivityIntent(this)
            startActivity(intent)
        }
    }

    private fun toggleEmptyState(state:Boolean){
        binding.emptyReview.isVisible = state
        binding.reviewsRecycler.isVisible=!state
    }
}