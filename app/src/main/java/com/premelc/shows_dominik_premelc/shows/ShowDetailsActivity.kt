package com.premelc.shows_dominik_premelc.shows

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.ActivityShowDetailsBinding
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.shows.ShowsActivity.Companion.buildShowsActivityIntent
import java.io.Serializable
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

        val username = intent.extras?.getString("username")
        initBackButton()
        initDetails(
            intent.extras?.getString("name"),
            intent.extras?.getString("description"),
            intent.extras?.getInt("img"),
        )
        initReviewsRecycler(intent.extras?.getSerializable("reviews"))
        initRatingDisplay()
        initReviewDialogButton(username)
    }

    private fun initReviewsRecycler(reviews: Serializable?) {
        adapter = ReviewsAdapter(this, emptyList())
        binding.reviewsRecycler.layoutManager = LinearLayoutManager(this)
        binding.reviewsRecycler.adapter = adapter
        binding.reviewsRecycler.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        adapter.addAllReviews(reviews as List<Review>)
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
            val intent = buildShowsActivityIntent(this)
            startActivity(intent)
        }
    }

    private fun initReviewDialogButton(username: String?) {
        binding.writeReviewButton.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.show_details_bottom_sheet, null)
            val btnClose = view.findViewById<ImageView>(R.id.closeButton)
            val btnSubmit = view.findViewById<MaterialButton>(R.id.submitReviewButton)

            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            btnSubmit.setOnClickListener {
                val comment = view.findViewById<TextInputEditText>(R.id.reviewInput).text.toString()
                val rating = view.findViewById<RatingBar>(R.id.ratingBar).rating
                addReviewToList(username, username, comment, rating)
                Toast.makeText(this, "Submited review successfully", Toast.LENGTH_SHORT).show()
                initRatingDisplay()
                dialog.dismiss()
            }

            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }
    }

    private fun toggleEmptyState(state: Boolean) {
        binding.emptyReview.isVisible = state
        binding.reviewsRecycler.isVisible = !state
    }

    private fun addReviewToList(id: String? = "placeholder", username: String? = "placeholder", text: String, rating: Float) {
        adapter.addItem(Review(id, username, rating, text, R.mipmap.pfp))
    }

    private fun initRatingDisplay() {
        val list = adapter.getAllReviews()
        var sum = 0F
        val df = DecimalFormat("#.##")
        for (item in list) {
            sum += item.grade
        }

        val reviewText = "${list.count()} reviews ${df.format(sum / list.count())} average"
        binding.ratings.rating = sum / list.count()
        binding.reviewsNumber.text = reviewText
    }

}