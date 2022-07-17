package com.premelc.shows_dominik_premelc.shows

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.ActivityShowDetailsBinding
import com.premelc.shows_dominik_premelc.databinding.ShowDetailsBottomSheetBinding
import com.premelc.shows_dominik_premelc.model.Review
import java.text.DecimalFormat

class ShowDetailsActivity : AppCompatActivity() {

    companion object {
        fun buildShowDetailsActivityIntent(activity: Activity): Intent {
            return Intent(activity, ShowDetailsActivity::class.java)
        }
    }

    private lateinit var binding: ActivityShowDetailsBinding
    private lateinit var adapter: ReviewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val username = intent.extras?.getString("username").toString()
        initBackButton()
        initDetails(
            intent.extras?.getString("name"),
            intent.extras?.getString("description"),
            intent.extras?.getInt("img"),
        )
        initReviewsRecycler(intent.extras?.getSerializable("reviews") as List<Review>)
        initRatingDisplay()
        initReviewDialogButton(username)
    }

    private fun initReviewsRecycler(reviews: List<Review>) {
        adapter = ReviewsAdapter(emptyList())
        binding.reviewsRecycler.layoutManager = LinearLayoutManager(this)
        binding.reviewsRecycler.adapter = adapter
        binding.reviewsRecycler.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        adapter.addAllReviews(reviews)
        if (adapter.itemCount > 0) toggleEmptyState(false) else toggleEmptyState(true)

    }

    private fun initDetails(name: String?, description: String?, @DrawableRes img: Int?) {
        if (img != null) {
            binding.img.setImageResource(img)
        }
        binding.showTitle.text = name
        binding.showDescription.text = description
    }

    private fun initBackButton() {
        binding.backArrow.setOnClickListener {
            finish()
        }
    }

    private fun initReviewDialogButton(username: String) {
        binding.writeReviewButton.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val bottomSheetBinding :ShowDetailsBottomSheetBinding = ShowDetailsBottomSheetBinding.inflate(layoutInflater)
            dialog.setContentView(bottomSheetBinding.root)
            val btnClose = bottomSheetBinding.closeButton
            val btnSubmit = bottomSheetBinding.submitReviewButton

            btnClose.setOnClickListener {

                dialog.dismiss()
            }
            btnSubmit.setOnClickListener {
                val comment = bottomSheetBinding.reviewInput.text.toString()
                val rating = bottomSheetBinding.ratingBar.rating
                addReviewToList(username, username, comment, rating)
                Toast.makeText(this, R.string.toast_make_review, Toast.LENGTH_SHORT).show()
                initRatingDisplay()
                dialog.dismiss()
            }

            dialog.setContentView(bottomSheetBinding.root)
            dialog.show()
        }
    }

    private fun toggleEmptyState(state: Boolean) {
        binding.emptyReview.isVisible = state
        binding.reviewsRecycler.isVisible = !state
    }

    private fun addReviewToList(id: String = "placeholder", username: String = "placeholder", text: String, rating: Float) {
        adapter.addItem(Review(id, username, rating, text, R.mipmap.pfp))
    }

    private fun initRatingDisplay() {
        val list = adapter.getAllReviews()
        var sum = 0F
        val df = DecimalFormat("#.##")
        for (item in list) {
            sum += item.grade
        }
        val count = list.count()
        val avg = df.format(sum / list.count())
        val reviewText = String.format(this.getString(R.string.reviewCount) , count , avg)
        println(this.getString(R.string.reviewCount))
        println(reviewText)
        binding.ratings.rating = sum / list.count()
        binding.reviewsNumber.text = reviewText
    }

}