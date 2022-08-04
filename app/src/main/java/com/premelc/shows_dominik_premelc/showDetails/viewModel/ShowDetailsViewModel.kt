package com.premelc.shows_dominik_premelc.showDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.premelc.shows_dominik_premelc.db.ReviewEntity
import com.premelc.shows_dominik_premelc.db.ShowEntity
import com.premelc.shows_dominik_premelc.db.ShowsDatabase
import com.premelc.shows_dominik_premelc.model.LoginResponse
import com.premelc.shows_dominik_premelc.model.PostReviewErrorResponse
import com.premelc.shows_dominik_premelc.model.PostReviewRequest
import com.premelc.shows_dominik_premelc.model.PostReviewResponse
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.model.ReviewsErrorResponse
import com.premelc.shows_dominik_premelc.model.ReviewsResponse
import com.premelc.shows_dominik_premelc.model.Show
import com.premelc.shows_dominik_premelc.model.ShowDetailsErrorResponse
import com.premelc.shows_dominik_premelc.model.ShowDetailsResponse
import com.premelc.shows_dominik_premelc.model.User
import com.premelc.shows_dominik_premelc.networking.ApiModule
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    fun initDetails(id: String) {
        viewModelScope.launch {
            repo.initDetails(id)
        }
    }

    fun loadReviewsToDb(reviews:List<Review>){
        viewModelScope.launch {
            repo.loadReviewsToDb(reviews)
        }
    }

    fun postReview(rating: Int, comment: String, showId: Int, userId: String) {
        repo.postReview(rating, comment , showId , userId)
    }
}