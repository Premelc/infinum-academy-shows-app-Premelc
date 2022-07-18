package com.premelc.shows_dominik_premelc.shows

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.ActivityShowDetailsBinding
import com.premelc.shows_dominik_premelc.databinding.ShowDetailsBottomSheetBinding
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.model.Show
import java.text.DecimalFormat

class ShowDetailsActivity : AppCompatActivity() {

    companion object {
        fun buildShowDetailsActivityIntent(activity: Activity): Intent {
            return Intent(activity, ShowDetailsActivity::class.java)
        }
    }

    private lateinit var binding: ActivityShowDetailsBinding
    private lateinit var adapter: ReviewsAdapter
    private lateinit var show: Show

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.extras?.getString("id")
        for (item in ListOfShows().shows){
            if (item.id == id)show=item; break
        }
        val username = intent.extras?.getString("username").toString()
        initBackButton()
        initDetails()
        initReviewsRecycler(show.reviews)
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
        if (adapter.itemCount > 0) togggleReviewsRecyclerFullOrEmpty(false) else togggleReviewsRecyclerFullOrEmpty(true)

    }

    private fun initDetails() {
            binding.img.setImageResource(show.imageResourceId)
            binding.showTitle.text = show.name
            binding.showDescription.text = show.description
    }

    private fun initBackButton() {
        binding.toolbar.setNavigationOnClickListener {
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

    private fun togggleReviewsRecyclerFullOrEmpty(isEmpty: Boolean) {
        binding.emptyReview.isVisible = isEmpty
        binding.reviewsRecycler.isVisible = !isEmpty
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