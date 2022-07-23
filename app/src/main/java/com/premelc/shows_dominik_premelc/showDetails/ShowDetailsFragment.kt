package com.premelc.shows_dominik_premelc.showDetails

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import com.premelc.shows_dominik_premelc.shows.ShowsViewModel

class ShowDetailsFragment : Fragment() {
    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ShowDetailsFragmentArgs>()
    private lateinit var adapter: ReviewsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel by viewModels<ShowDetailsViewModel>()
    private val showsViewModel by viewModels<ShowsViewModel>()

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
        viewModel.setShow(showsViewModel.findShowById(args.id)!!)
        initializeUI()
    }

    private fun initializeUI() {
        initBackButton()
        initDetails()
        initReviewsRecycler(viewModel.show.value!!.reviews)
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

    private fun initReviewDialogButton(username: String) {
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
                addReviewToList(username, username, comment, rating)
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

    private fun addReviewToList(id: String = "placeholder", username: String = "placeholder", text: String, rating: Float) {
        adapter.addItem(Review(id, username, rating, text, R.mipmap.pfp))
        adapter.notifyDataSetChanged()
    }

    private fun initRatingDisplay() {
        viewModel.initRatingDisplay(adapter.getAllReviews(), binding.ratings, binding.reviewsNumber)
    }
}