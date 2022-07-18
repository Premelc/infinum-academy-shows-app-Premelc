package com.premelc.shows_dominik_premelc.shows

import androidx.navigation.fragment.navArgs
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.FragmentShowDetailsBinding
import com.premelc.shows_dominik_premelc.databinding.ShowDetailsBottomSheetBinding
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.model.Show
import java.text.DecimalFormat

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ShowDetailsFragmentArgs>()
    private lateinit var adapter: ReviewsAdapter
    private lateinit var show: Show

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment()
    }

    private fun initializeFragment(){
        val id = args.id
        for (item in ListOfShows().shows) {
            if (item.id == id) show = item;
        }
        val username = args.username
        initBackButton()
        initDetails()
        initReviewsRecycler(show.reviews)
        initRatingDisplay()
        initReviewDialogButton(username)
    }

    private fun initReviewsRecycler(reviews: List<Review>) {
        adapter = ReviewsAdapter(emptyList())
        binding.reviewsRecycler.layoutManager = LinearLayoutManager(context)
        binding.reviewsRecycler.adapter = adapter
        binding.reviewsRecycler.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
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
        binding.toolbar.setNavigationIcon(R.drawable.arrow)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initReviewDialogButton(username: String) {
        binding.writeReviewButton.setOnClickListener {
            val dialog = context?.let { it1 -> BottomSheetDialog(it1) }
            val bottomSheetBinding: ShowDetailsBottomSheetBinding = ShowDetailsBottomSheetBinding.inflate(layoutInflater)
            dialog?.setContentView(bottomSheetBinding.root)
            val btnClose = bottomSheetBinding.closeButton
            val btnSubmit = bottomSheetBinding.submitReviewButton

            btnClose.setOnClickListener {
                dialog?.dismiss()
            }
            btnSubmit.setOnClickListener {
                val comment = bottomSheetBinding.reviewInput.text.toString()
                val rating = bottomSheetBinding.ratingBar.rating
                addReviewToList(username, username, comment, rating)
                Toast.makeText(context, R.string.toast_make_review, Toast.LENGTH_SHORT).show()
                initRatingDisplay()
                dialog?.dismiss()
            }
            dialog?.setContentView(bottomSheetBinding.root)
            dialog?.show()
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
        val reviewText = String.format(this.getString(R.string.reviewCount), count, avg)
        binding.ratings.rating = sum / list.count()
        binding.reviewsNumber.text = reviewText
    }

}