package com.premelc.shows_dominik_premelc.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.premelc.shows_dominik_premelc.db.ShowEntity
import com.premelc.shows_dominik_premelc.db.ShowsDatabase
import com.premelc.shows_dominik_premelc.model.ChangePhotoErrorResponse
import com.premelc.shows_dominik_premelc.model.LoginResponse
import com.premelc.shows_dominik_premelc.model.Show
import com.premelc.shows_dominik_premelc.model.ShowsErrorResponse
import com.premelc.shows_dominik_premelc.model.ShowsResponse
import com.premelc.shows_dominik_premelc.model.TopRatedShowsResponse
import com.premelc.shows_dominik_premelc.networking.ApiModule
import java.io.File
import java.util.concurrent.Executors
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
        ApiModule.retrofit.getMe().enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _connectionEstablished.value = true
                fetchShowsFromServer()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _connectionEstablished.value = false
            }
        })

    }

    fun fetchShowsFromServer() {
        ApiModule.retrofit.shows().enqueue(object : Callback<ShowsResponse> {
            override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                if (response.isSuccessful) {
                    _showsResponse.value = response.isSuccessful
                    _shows.value = response.body()?.shows
                    _showsRecyclerFullOrEmpty.value = shows.value?.isEmpty()
                        addToDb(_shows.value!!.map { shows ->
                            ShowEntity(
                                shows.id,
                                shows.average_rating,
                                shows.description.toString(),
                                shows.image_url,
                                shows.no_of_reviews,
                                shows.title
                            )
                        })
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

    fun addToDb(list: List<ShowEntity>){
        Executors.newSingleThreadExecutor().execute {
            database.showsDAO().insertAllShows(list)
        }
    }

    fun fetchShowsFromDb(): LiveData<List<ShowEntity>> {
        return database.showsDAO().getAllShows()
    }

    fun fetchTopRatedShowsFromServer() {
        ApiModule.retrofit.topRatedShows().enqueue(object : Callback<TopRatedShowsResponse> {
            override fun onResponse(call: Call<TopRatedShowsResponse>, response: Response<TopRatedShowsResponse>) {
                if (response.isSuccessful) {
                    _showsResponse.value = response.isSuccessful
                    _shows.value = response.body()?.shows
                    _showsRecyclerFullOrEmpty.value = shows.value?.isEmpty()
                    addToDb(_shows.value!!.map { shows ->
                        ShowEntity(
                            shows.id,
                            shows.average_rating,
                            shows.description.toString(),
                            shows.image_url,
                            shows.no_of_reviews,
                            shows.title
                        )
                    })
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
            .addFormDataPart("image", "avatar.jpg", file.asRequestBody(MEDIA_TYPE_JPG))
            .build()

        ApiModule.retrofit.changePhoto(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _changePhotoResponse.value = response.isSuccessful
                    _changePhotoResponseMessage.value = response.body()?.user?.image_url
                } else {
                    val gson = Gson()
                    val changePhotoErrorResponse: ChangePhotoErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), ChangePhotoErrorResponse::class.java)
                    _showsErrorMessage.value = changePhotoErrorResponse.errors.first()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _changePhotoResponse.value = false
            }
        })
    }
}