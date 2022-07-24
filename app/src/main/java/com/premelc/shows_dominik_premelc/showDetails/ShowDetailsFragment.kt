package com.premelc.shows_dominik_premelc.showDetails

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.FragmentShowDetailsBinding
import com.premelc.shows_dominik_premelc.databinding.ShowDetailsBottomSheetBinding
import com.premelc.shows_dominik_premelc.model.Review
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class ShowDetailsFragment : Fragment() {
    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ShowDetailsFragmentArgs>()
    private lateinit var adapter: ReviewsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel by viewModels<ShowDetailsViewModel>()
    private lateinit var reviewsList: List<Review>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("SHOWS", Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setId(args.id)
        viewModel.show.observe(viewLifecycleOwner) { show ->
            binding.showTitle.text = show.name
            binding.showDescription.text = show.description
            binding.img.setImageResource(show.imageResourceId)
        }
        viewModel.reviewCount.observe(viewLifecycleOwner) { reviewCount ->
            binding.reviewsNumber.text = String.format(this.getString(R.string.reviewCount), reviewCount, viewModel.reviewAvg.value)
        }
        viewModel.rating.observe(viewLifecycleOwner) { rating ->
            binding.ratings.rating = rating
        }
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            reviewsList = reviews
            adapter.addAllReviews(reviews)
            adapter.notifyDataSetChanged()
        }
        reviewsList = viewModel.reviews.value!!
        initializeUI()
    }

    private fun initializeUI() {
        initBackButton()
        initDetails()
        initReviewsRecycler(reviewsList)
        initRatingDisplay()
        initReviewDialogButton(args.username)
    }

    private fun initBackButton() {
        binding.toolbar.setNavigationIcon(R.drawable.arrow)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initReviewsRecycler(reviews: List<Review>) {
        adapter = ReviewsAdapter(reviews)
        binding.reviewsRecycler.layoutManager = LinearLayoutManager(context)
        binding.reviewsRecycler.adapter = adapter
        binding.reviewsRecycler.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
        if (adapter.itemCount > 0) toggleReviewsRecyclerFullOrEmpty(false) else toggleReviewsRecyclerFullOrEmpty(true)
    }

    private fun initDetails() {
        binding.img.setImageResource(viewModel.show.value!!.imageResourceId)
        binding.showTitle.text = viewModel.show.value!!.name
        binding.showDescription.text = viewModel.show.value!!.description
    }

    private fun initReviewDialogButton(usr: String) {
        val username = when (usr) {
            "" -> sharedPreferences.getString("USERNAME", "placeholder")
            else -> usr
        }
        binding.writeReviewButton.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            val bottomSheetBinding: ShowDetailsBottomSheetBinding = ShowDetailsBottomSheetBinding.inflate(layoutInflater)
            dialog.setContentView(bottomSheetBinding.root)
            val btnClose = bottomSheetBinding.closeButton
            val btnSubmit = bottomSheetBinding.submitReviewButton
            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            btnSubmit.setOnClickListener {
                val comment = bottomSheetBinding.reviewInput.text.toString()
                val rating = bottomSheetBinding.ratingBar.rating
                val review = Review(username!!, username, rating, comment, R.mipmap.pfp)
                addReviewToList(review)
                Toast.makeText(context, R.string.toast_make_review, Toast.LENGTH_SHORT).show()
                initRatingDisplay()
                dialog.dismiss()
            }
            dialog.setContentView(bottomSheetBinding.root)
            dialog.show()
        }
    }

    private fun toggleReviewsRecyclerFullOrEmpty(isEmpty: Boolean) {
        binding.emptyReview.isVisible = isEmpty
        binding.reviewsRecycler.isVisible = !isEmpty
    }

    private fun addReviewToList(review: Review) {
        viewModel.addReview(review)
        adapter.notifyItemInserted(viewModel.reviews.value!!.size)
    }

    private fun initRatingDisplay() {
        viewModel.initRatingDisplay()
    }
}