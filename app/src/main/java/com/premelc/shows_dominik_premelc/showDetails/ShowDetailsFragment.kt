package com.premelc.shows_dominik_premelc.showDetails

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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.FragmentShowDetailsBinding
import com.premelc.shows_dominik_premelc.databinding.LoadingBottomSheetBinding
import com.premelc.shows_dominik_premelc.databinding.RequestResponseBottomSheetBinding
import com.premelc.shows_dominik_premelc.databinding.ShowDetailsBottomSheetBinding
import com.premelc.shows_dominik_premelc.model.Review

class ShowDetailsFragment : Fragment() {
    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ShowDetailsFragmentArgs>()
    private lateinit var adapter: ReviewsAdapter
    private val viewModel by viewModels<ShowDetailsViewModel>()
    private lateinit var dialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        dialog = BottomSheetDialog(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.show.observe(viewLifecycleOwner) { show ->
            binding.showTitle.text = show.title
            binding.showDescription.text = show.description
            binding.reviewsNumber.text = String.format(
                this.getString(R.string.reviewCount),
                show.no_of_reviews.toString(),
                show.average_rating.toString()
            )
            if (show.average_rating != null) binding.ratings.rating = show.average_rating.toFloat()
            else binding.ratings.rating = show.no_of_reviews.toFloat()
            Glide.with(requireContext())
                .load(show.image_url)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.img)
        }
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            adapter.addAllReviews(reviews)
            adapter.notifyDataSetChanged()
        }
        viewModel.reviewsRecyclerFullOrEmpty.observe(viewLifecycleOwner) { toggleReviewsRecyclerFullOrEmpty ->
            toggleReviewsRecyclerFullOrEmpty(toggleReviewsRecyclerFullOrEmpty)
        }
        viewModel.reviewsResponse.observe(viewLifecycleOwner) { reviewsResponse ->
            dialog.dismiss()
            if (reviewsResponse != "true") {
                val bottomSheetBinding: RequestResponseBottomSheetBinding = RequestResponseBottomSheetBinding.inflate(layoutInflater)
                with(bottomSheetBinding) {
                    callbackIcon.setImageResource(R.drawable.fail)
                    callbackText.text = getString(R.string.reviews_fetch_failed)
                    if (reviewsResponse == "false")callbackDescription.text = getString(R.string.connection_error)
                    else callbackDescription.text = reviewsResponse
                }
                dialog.setContentView(bottomSheetBinding.root)
                dialog.show()
            }
        }
        viewModel.postReviewResponse.observe(viewLifecycleOwner) { postReviewResponse ->
            dialog.dismiss()
            if (postReviewResponse != "true") {
                val bottomSheetBinding: RequestResponseBottomSheetBinding = RequestResponseBottomSheetBinding.inflate(layoutInflater)
                with(bottomSheetBinding) {
                    callbackIcon.setImageResource(R.drawable.fail)
                    callbackText.text = getString(R.string.post_review_failed)
                    if (postReviewResponse == "false")callbackDescription.text = getString(R.string.connection_error)
                    else callbackDescription.text = postReviewResponse
                }
                dialog.setContentView(bottomSheetBinding.root)
                dialog.show()
            }else{
                Toast.makeText(context, R.string.toast_make_review, Toast.LENGTH_SHORT).show()
            }
        }
        initializeUI()
    }

    private fun initializeUI() {
        initLoadingBottomSheet()
        initBackButton()
        initDetails()
        initReviewsRecycler(emptyList())
        initReviewDialogButton()
    }

    private fun initLoadingBottomSheet() {
        val loadingBottomSheetBinding: LoadingBottomSheetBinding = LoadingBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(loadingBottomSheetBinding.root)
        dialog.show()
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
    }

    private fun initDetails() {
        viewModel.initDetails(args.id)
    }

    private fun initReviewDialogButton() {
        binding.writeReviewButton.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            val bottomSheetBinding: ShowDetailsBottomSheetBinding =
                ShowDetailsBottomSheetBinding.inflate(layoutInflater)
            dialog.setContentView(bottomSheetBinding.root)
            val btnClose = bottomSheetBinding.closeButton
            val btnSubmit = bottomSheetBinding.submitReviewButton
            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            btnSubmit.setOnClickListener {
                val comment = bottomSheetBinding.reviewInput.text.toString()
                val rating = bottomSheetBinding.ratingBar.rating.toInt()
                viewModel.postReview(rating, comment, args.id.toInt())
                dialog.dismiss()
                initLoadingBottomSheet()
            }
            dialog.setContentView(bottomSheetBinding.root)
            dialog.show()
        }
    }

    private fun toggleReviewsRecyclerFullOrEmpty(isEmpty: Boolean) {
        binding.emptyReview.isVisible = !isEmpty
        binding.reviewsRecycler.isVisible = isEmpty
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}