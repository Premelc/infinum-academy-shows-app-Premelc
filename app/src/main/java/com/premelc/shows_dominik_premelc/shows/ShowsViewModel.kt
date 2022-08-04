package com.premelc.shows_dominik_premelc.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
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
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val MEDIA_TYPE_JPG = "image/png".toMediaType()

class ShowsViewModel(
    private val database: ShowsDatabase
) : ViewModel() {

    private val _shows = MutableLiveData<List<Show>>(emptyList())
    val shows: LiveData<List<Show>> = _shows

    private val _showsRecyclerFullOrEmpty = MutableLiveData<Boolean>()
    val showsRecyclerFullOrEmpty: LiveData<Boolean> = _showsRecyclerFullOrEmpty

    private val _showsResponse = MutableLiveData<Boolean>()
    val showsResponse: LiveData<Boolean> = _showsResponse

    private val _showsErrorMessage = MutableLiveData<String>()
    val showsErrorMessage: LiveData<String> = _showsErrorMessage

    private val _changePhotoResponse = MutableLiveData<Boolean>()
    val changePhotoResponse: LiveData<Boolean> = _changePhotoResponse

    private val _changePhotoResponseMessage = MutableLiveData<String>()
    val changePhotoResponseMessage: LiveData<String> = _changePhotoResponseMessage

    private var _connectionEstablished = MutableLiveData<Boolean>()
    var connectionEstablished: LiveData<Boolean> = _connectionEstablished

    init {
        checkIsServerResponsive()
    }

    private fun checkIsServerResponsive() {
        ApiModule.retrofit.getMe().enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _connectionEstablished.value = true
                fetchShowsFromServer()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _connectionEstablished.value = false
                viewModelScope.launch {
                    fetchAllShowsFromDb()
                }
            }
        })
    }

    fun submitPendingReviews(userEmail: String) {
        viewModelScope.launch {
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
                            viewModelScope.launch {
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

    private suspend fun deleteReview(reviewId: String) {
        database.reviewsDAO().deleteByReviewId(reviewId)
    }

    fun fetchShowsFromServer() {
        ApiModule.retrofit.shows().enqueue(object : Callback<ShowsResponse> {
            override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                if (response.isSuccessful) {
                    _showsResponse.value = response.isSuccessful
                    _shows.value = response.body()?.shows
                    _showsRecyclerFullOrEmpty.value = shows.value?.isEmpty()
                    viewModelScope.launch {
                        _shows.value?.let { shows ->
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
                    }
                } else {
                    val gson = Gson()
                    val showsErrorResponse: ShowsErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), ShowsErrorResponse::class.java)
                    _showsErrorMessage.value = showsErrorResponse.errors.first()
                }
            }

            override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                _showsResponse.value = false
            }
        })
    }

    suspend fun addToDb(list: List<ShowEntity>) {
        database.showsDAO().insertAllShows(list)
    }

    suspend fun fetchAllShowsFromDb() {
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
        _showsRecyclerFullOrEmpty.value = shows.value?.isEmpty()
    }

    fun fetchTopRatedShowsFromServer() {
        ApiModule.retrofit.topRatedShows().enqueue(object : Callback<TopRatedShowsResponse> {
            override fun onResponse(call: Call<TopRatedShowsResponse>, response: Response<TopRatedShowsResponse>) {
                if (response.isSuccessful) {
                    _showsResponse.value = response.isSuccessful
                    _shows.value = response.body()?.shows
                    _showsRecyclerFullOrEmpty.value = shows.value?.isEmpty()
                    viewModelScope.launch {
                        _shows.value?.let { shows ->
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
                    }
                } else {
                    val gson = Gson()
                    val showsErrorResponse: ShowsErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), ShowsErrorResponse::class.java)
                    _showsErrorMessage.value = showsErrorResponse.errors.first()
                }
            }

            override fun onFailure(call: Call<TopRatedShowsResponse>, t: Throwable) {
                _showsResponse.value = false
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