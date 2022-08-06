package com.premelc.shows_dominik_premelc.showDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.premelc.shows_dominik_premelc.db.ReviewEntity
import com.premelc.shows_dominik_premelc.db.ShowsDatabase
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.model.Show
import kotlinx.coroutines.launch

class ShowDetailsViewModel(
    private val database: ShowsDatabase
) : ViewModel() {

    private val repo = ShowDetailsViewModelRepository(database)
    val show: LiveData<Show> = repo.getShows()
    val reviews: LiveData<List<Review>> = repo.getReviews()
    val reviewsRecyclerFullOrEmpty: LiveData<Boolean> = repo.getReviewsRecyclerFullOrEmpty()
    val showsDetailResponse: LiveData<Boolean> = repo.getShowsDetailsResponse()
    val showsDetailErrorMessage: LiveData<String> = repo.getShowsDetailsErrorMessage()
    val reviewsResponse: LiveData<Boolean> = repo.getReviewsResponse()
    val reviewsErrorMessage: LiveData<String> = repo.getReviewsErrorMessage()
    val postReviewResponse: LiveData<Boolean> = repo.getPostReviewResponse()
    var postReviewErrorMessage: LiveData<String> = repo.getPostReviewErrorMessage()
    var connectionEstablished: LiveData<Boolean> = repo.getConnectionEstablished()
    var postedReview: LiveData<ReviewEntity> = repo.getPostedReview()

    fun initDetails(id: String) {
        viewModelScope.launch {
            repo.fetchShowDetailsAndReviews(id)
        }
    }

    fun loadReviewsToDb(reviews: List<Review>) {
        viewModelScope.launch {
            repo.loadReviewsToDb(reviews)
        }
    }

    fun postReview(rating: Int, comment: String, showId: Int, userId: String) {
        repo.submitReviewToServer(rating, comment, showId, userId)
    }

    fun handleReview(review: ReviewEntity) {
        viewModelScope.launch {
            repo.addReviewToDb(review)
        }
    }
}