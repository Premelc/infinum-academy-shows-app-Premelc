package com.premelc.shows_dominik_premelc.shows.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.premelc.shows_dominik_premelc.MEDIA_TYPE_JPG
import com.premelc.shows_dominik_premelc.db.ShowEntity
import com.premelc.shows_dominik_premelc.db.ShowsDatabase
import com.premelc.shows_dominik_premelc.model.ChangePhotoErrorResponse
import com.premelc.shows_dominik_premelc.model.LoginResponse
import com.premelc.shows_dominik_premelc.model.PostReviewRequest
import com.premelc.shows_dominik_premelc.model.PostReviewResponse
import com.premelc.shows_dominik_premelc.model.Show
import com.premelc.shows_dominik_premelc.model.ShowsErrorResponse
import com.premelc.shows_dominik_premelc.model.ShowsResponse
import com.premelc.shows_dominik_premelc.model.TopRatedShowsResponse
import com.premelc.shows_dominik_premelc.networking.ApiModule
import java.io.File
import java.util.concurrent.Executors
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModelRepository(private val database: ShowsDatabase) {

    private val _shows = MutableLiveData<List<Show>>(emptyList())
    private val _showsRecyclerFullOrEmpty = MutableLiveData<Boolean>()
    private val _showsResponse = MutableLiveData<Boolean>()
    private val _showsErrorMessage = MutableLiveData<String>()
    private val _changePhotoResponse = MutableLiveData<Boolean>()
    private val _changePhotoResponseMessage = MutableLiveData<String>()
    private var _connectionEstablished = MutableLiveData<Boolean>()

    fun getShows() = _shows
    fun getShowsRecyclerFullOrEmpty() = _showsRecyclerFullOrEmpty
    fun getShowsResponse() = _showsResponse
    fun getShowsErrorMessage() = _showsErrorMessage
    fun getChangePhotoResponse() = _changePhotoResponse
    fun getChangePhotoResponseMessage() = _changePhotoResponseMessage
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

    suspend fun fetchShows() {
        if (getConnectionEstablished().value == true) {
            fetchShowsFromServer()
        } else {
            fetchAllShowsFromDb()
        }
    }

    fun submitPendingReviews(userEmail: String) {
        Executors.newSingleThreadExecutor().execute {
            val pendingReviews = database.reviewsDAO().getPendingReviews(true, userEmail)
            if (pendingReviews.isNotEmpty()) {
                for (review in pendingReviews) {
                    val postReviewRequest = PostReviewRequest(
                        review.rating,
                        review.comment,
                        review.showId
                    )
                    ApiModule.retrofit.postReview(postReviewRequest).enqueue(object : Callback<PostReviewResponse> {
                        override fun onResponse(call: Call<PostReviewResponse>, response: Response<PostReviewResponse>) {
                            Executors.newSingleThreadExecutor().execute {
                                deleteReview(review.id)
                            }
                        }

                        override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                            _connectionEstablished.value = false
                        }
                    })
                }
            }
        }
    }

    private fun deleteReview(reviewId: String) {
        database.reviewsDAO().deleteByReviewId(reviewId)
    }

    private fun fetchShowsFromServer() {
        ApiModule.retrofit.shows().enqueue(object : Callback<ShowsResponse> {
            override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                if (response.isSuccessful) {
                    _showsResponse.value = response.isSuccessful
                    _shows.value = response.body()?.shows
                    _showsRecyclerFullOrEmpty.value = getShows().value?.isEmpty()
                    _connectionEstablished.value = true
                } else {
                    val gson = Gson()
                    val showsErrorResponse: ShowsErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), ShowsErrorResponse::class.java)
                    _showsErrorMessage.value = showsErrorResponse.errors.first()
                }
            }

            override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                _connectionEstablished.value = false
                _showsResponse.value = false
            }
        })
    }

    suspend fun addToDb(list: List<ShowEntity>) {
        database.showsDAO().insertAllShows(list)
    }

    private suspend fun fetchAllShowsFromDb() {
        _shows.value = database.showsDAO().getAllShows().map { showEntity ->
            Show(
                showEntity.id,
                showEntity.averageRating,
                showEntity.description,
                showEntity.imageUrl,
                showEntity.noOfReviews,
                showEntity.title
            )
        }
        _showsRecyclerFullOrEmpty.value = getShows().value?.isEmpty()
    }

    suspend fun fetchTopRatedShows() {
        fetchTopRatedShowsFromServer()
        if (getConnectionEstablished().value == false) {
            fetchAllShowsFromDb()
        }
    }

    suspend fun loadShowsToDb(shows: List<Show>) {
        addToDb(shows.map { show ->
            ShowEntity(
                show.id,
                show.average_rating,
                show.description.toString(),
                show.image_url,
                show.no_of_reviews,
                show.title
            )
        })
    }

    private fun fetchTopRatedShowsFromServer() {
        ApiModule.retrofit.topRatedShows().enqueue(object : Callback<TopRatedShowsResponse> {
            override fun onResponse(call: Call<TopRatedShowsResponse>, response: Response<TopRatedShowsResponse>) {
                if (response.isSuccessful) {
                    _showsResponse.value = response.isSuccessful
                    _shows.value = response.body()?.shows
                    _showsRecyclerFullOrEmpty.value = getShows().value?.isEmpty()
                    _connectionEstablished.value = true
                } else {
                    val gson = Gson()
                    val showsErrorResponse: ShowsErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), ShowsErrorResponse::class.java)
                    _showsErrorMessage.value = showsErrorResponse.errors.first()
                }
            }

            override fun onFailure(call: Call<TopRatedShowsResponse>, t: Throwable) {
                _showsResponse.value = false
                _connectionEstablished.value = false
            }
        })
    }

    fun uploadImage(email: String, file: File) {
        val request = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("image", file.name, file.asRequestBody(MEDIA_TYPE_JPG))
            .build()

        ApiModule.retrofit.changePhoto(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _changePhotoResponse.value = response.isSuccessful
                    _changePhotoResponseMessage.value = response.body()?.user?.image_url.toString()
                } else {
                    val gson = Gson()
                    val changePhotoErrorResponse: ChangePhotoErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), ChangePhotoErrorResponse::class.java)
                    _showsErrorMessage.value = changePhotoErrorResponse.errors.first()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
                _changePhotoResponse.value = false
            }
        })
    }

}