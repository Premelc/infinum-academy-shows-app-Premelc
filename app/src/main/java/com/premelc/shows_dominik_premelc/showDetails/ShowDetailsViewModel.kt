package com.premelc.shows_dominik_premelc.showDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.premelc.shows_dominik_premelc.model.PostReviewErrorResponse
import com.premelc.shows_dominik_premelc.model.PostReviewRequest
import com.premelc.shows_dominik_premelc.model.PostReviewResponse
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.model.ReviewsErrorResponse
import com.premelc.shows_dominik_premelc.model.ReviewsResponse
import com.premelc.shows_dominik_premelc.model.Show
import com.premelc.shows_dominik_premelc.model.ShowDetailsErrorResponse
import com.premelc.shows_dominik_premelc.model.ShowDetailsResponse
import com.premelc.shows_dominik_premelc.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel : ViewModel() {
    private var _show = MutableLiveData<Show>()
    val show: LiveData<Show> = _show

    private var _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    private var _reviewsRecyclerFullOrEmpty = MutableLiveData<Boolean>()
    val reviewsRecyclerFullOrEmpty: LiveData<Boolean> = _reviewsRecyclerFullOrEmpty

    private var _showsDetailResponse = MutableLiveData<Boolean>()
    val showsDetailResponse: LiveData<Boolean> = _showsDetailResponse

    private var _showsDetailErrorMessage = MutableLiveData<String>()
    val showsDetailErrorMessage: LiveData<String> = _showsDetailErrorMessage

    private var _reviewsResponse = MutableLiveData<Boolean>()
    val reviewsResponse: LiveData<Boolean> = _reviewsResponse

    private var _reviewsErrorMessage = MutableLiveData<String>()
    val reviewsErrorMessage:LiveData<String> = _reviewsErrorMessage

    private var _postReviewResponse = MutableLiveData<Boolean>()
    val postReviewResponse: LiveData<Boolean> = _postReviewResponse

    private var _postReviewErrorMessage = MutableLiveData<String>()
    var postReviewErrorMessage: LiveData<String> = _postReviewErrorMessage

    fun initDetails(id: String) {
        fetchShow(id)
        fetchReviews(id.toInt())
    }

    private fun fetchShow(id: String) {
        ApiModule.retrofit.specificShow(id).enqueue(object : Callback<ShowDetailsResponse> {
            override fun onResponse(call: Call<ShowDetailsResponse>, response: Response<ShowDetailsResponse>) {
                if (response.isSuccessful) {
                    _show.value = response.body()?.show
                    _showsDetailResponse.value = response.isSuccessful
                    _reviewsRecyclerFullOrEmpty.value = response.isSuccessful
                } else {
                    val gson = Gson()
                    val showDetailsErrorResponse: ShowDetailsErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), ShowDetailsErrorResponse::class.java)
                    _showsDetailErrorMessage.value = showDetailsErrorResponse.errors.first()
                }
            }

            override fun onFailure(call: Call<ShowDetailsResponse>, t: Throwable) {
                _showsDetailResponse.value = false
            }
        })
    }

    private fun fetchReviews(id: Int) {
        ApiModule.retrofit.showReviews(id).enqueue(object : Callback<ReviewsResponse> {
            override fun onResponse(call: Call<ReviewsResponse>, response: Response<ReviewsResponse>) {
                if (response.isSuccessful) {
                    _reviews.value = response.body()?.reviews
                    _reviewsResponse.value = response.isSuccessful
                } else {
                    val gson = Gson()
                    val reviewsErrorResponse: ReviewsErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), ReviewsErrorResponse::class.java)
                    _reviewsErrorMessage.value = reviewsErrorResponse.errors.first()
                }
            }

            override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                _reviewsResponse.value = false
            }
        })
    }

    fun postReview(rating: Int, comment: String, showId: Int) {
        val postReviewRequest = PostReviewRequest(
            rating,
            comment,
            showId
        )

        ApiModule.retrofit.postReview(postReviewRequest).enqueue(object : Callback<PostReviewResponse> {
            override fun onResponse(call: Call<PostReviewResponse>, response: Response<PostReviewResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) addReview(response.body()!!.review)
                    _postReviewResponse.value = response.isSuccessful
                } else {
                    val gson = Gson()
                    val postReviewErrorResponse: PostReviewErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), PostReviewErrorResponse::class.java)
                    if (response.code() == 401) _postReviewErrorMessage.value = postReviewErrorResponse.errors.first()
                    else {
                        _postReviewErrorMessage.value = postReviewErrorResponse.errors[0] + " , " + postReviewErrorResponse.errors[1]
                    }
                }
            }
            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                _postReviewResponse.value = false
            }
        })
    }

    fun addReview(review: Review) {
        _reviews.value = reviews.value?.plus(review)
    }
}