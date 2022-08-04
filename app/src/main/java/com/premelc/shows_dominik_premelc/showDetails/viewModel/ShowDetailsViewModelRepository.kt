package com.premelc.shows_dominik_premelc.showDetails.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.premelc.shows_dominik_premelc.db.ReviewEntity
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
import java.util.concurrent.Executors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModelRepository(private val database: ShowsDatabase) {

    private var _connectionEstablished = MutableLiveData<Boolean>()
    private var _postReviewErrorMessage = MutableLiveData<String>()
    private var _postReviewResponse = MutableLiveData<Boolean>()
    private var _reviewsErrorMessage = MutableLiveData<String>()
    private var _reviewsResponse = MutableLiveData<Boolean>()
    private var _showsDetailErrorMessage = MutableLiveData<String>()
    private var _showsDetailResponse = MutableLiveData<Boolean>()
    private var _reviewsRecyclerFullOrEmpty = MutableLiveData<Boolean>()
    private var _reviews = MutableLiveData<List<Review>>()
    private var _show = MutableLiveData<Show>()

    fun getShows() = _show
    fun getReviews() = _reviews
    fun getReviewsRecyclerFullOrEmpty() = _reviewsRecyclerFullOrEmpty
    fun getShowsDetailsResponse() = _showsDetailResponse
    fun getShowsDetailsErrorMessage() = _showsDetailErrorMessage
    fun getReviewsResponse() = _reviewsResponse
    fun getReviewsErrorMessage() = _reviewsErrorMessage
    fun getPostReviewResponse() = _postReviewResponse
    fun getPostReviewErrorMessage() = _postReviewErrorMessage
    fun getConnectionEstablished() = _connectionEstablished

    init {
        checkIsServerResponsive()
    }

    private fun checkIsServerResponsive() {
        ApiModule.retrofit.getMe().enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _connectionEstablished.value = true
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _connectionEstablished.value = false
            }
        })
    }

    suspend fun initDetails(id: String) {
        if (getConnectionEstablished().value == true) {
            fetchShow(id)
            fetchReviews(id.toInt())
        } else {
            fetchShowFromDb(id)
            fetchReviewsFromDb(id.toInt())
        }
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

    suspend fun loadReviewsToDb(reviews: List<Review>) {
        addAllReviewsToDb(reviews.map { review ->
            ReviewEntity(
                review.id,
                review.comment ?: "",
                review.rating,
                review.show_id,
                review.user.id,
                review.user.email,
                review.user.image_url.toString()
            )
        })
    }

    private suspend fun addAllReviewsToDb(list: List<ReviewEntity>) {
        Executors.newSingleThreadExecutor().execute {
            database.reviewsDAO().insertReview(list)
        }
    }

    private suspend fun fetchShowFromDb(id: String) {
        val showEntity = database.showsDAO().getShow(id)
        _show.value = Show(
            showEntity.id,
            showEntity.averageRating,
            showEntity.description,
            showEntity.imageUrl,
            showEntity.noOfReviews,
            showEntity.title
        )
    }

    private suspend fun fetchReviewsFromDb(id: Int) {
        val reviewsEntity = database.reviewsDAO().getAllTheReviews(id)
        _reviews.value = reviewsEntity.map { reviewEntity ->
            Review(
                reviewEntity.id,
                reviewEntity.comment,
                reviewEntity.rating,
                reviewEntity.showId,
                User(
                    reviewEntity.userId,
                    reviewEntity.userEmail,
                    reviewEntity.userImageUrl
                )
            )
        }
        _reviewsRecyclerFullOrEmpty.value = reviewsEntity.isNotEmpty()
    }

    fun postReview(rating: Int, comment: String, showId: Int, userId: String) {
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
                    val review = response.body()?.review
                    if (review != null) {
                        Executors.newSingleThreadExecutor().execute {
                            addReviewToDb(
                                ReviewEntity(
                                    review.id,
                                    review.comment,
                                    review.rating,
                                    review.show_id,
                                    review.user.id,
                                    review.user.email,
                                    review.user.image_url.toString()
                                )
                            )
                        }
                    }
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
                Executors.newSingleThreadExecutor().execute {
                    addReviewToDb(
                        ReviewEntity(
                            (comment + userId + showId.toString()),
                            comment,
                            rating,
                            showId,
                            userId.substringBefore('@'),
                            userId,
                            "default",
                            true
                        )
                    )
                }
            }
        })
    }

    fun addReviewToDb(review: ReviewEntity) {
        database.reviewsDAO().insertReview(listOf(review))
    }

    fun addReview(review: Review) {
        _reviews.value = _reviews.value?.plus(review)
    }

}