package com.premelc.shows_dominik_premelc.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.premelc.shows_dominik_premelc.model.LoginResponse
import com.premelc.shows_dominik_premelc.model.Show
import com.premelc.shows_dominik_premelc.model.ShowsErrorResponse
import com.premelc.shows_dominik_premelc.model.ShowsResponse
import com.premelc.shows_dominik_premelc.model.TopRatedShowsResponse
import com.premelc.shows_dominik_premelc.networking.ApiModule
import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val MEDIA_TYPE_JPG = "image/png".toMediaType()

class ShowsViewModel : ViewModel() {

    private val _shows = MutableLiveData<List<Show>>(emptyList())
    val shows: LiveData<List<Show>> = _shows

    private val _showsRecyclerFullOrEmpty = MutableLiveData<Boolean>()
    val showsRecyclerFullOrEmpty: LiveData<Boolean> = _showsRecyclerFullOrEmpty

    private val _showsResponse = MutableLiveData<String>()
    val showsResponse: LiveData<String> = _showsResponse

    private val _changePhotoResponse = MutableLiveData<String>()
    val changePhotoResponse: LiveData<String> = _changePhotoResponse

    init {
        fetchShowsFromServer()
    }

    fun fetchShowsFromServer() {
        ApiModule.retrofit.shows().enqueue(object : Callback<ShowsResponse> {
            override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                if (response.isSuccessful) {
                    _showsResponse.value = response.isSuccessful.toString()
                    _shows.value = response.body()?.shows
                    _showsRecyclerFullOrEmpty.value = shows.value?.isEmpty()
                } else {
                    val gson = Gson()
                    val showsErrorResponse: ShowsErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), ShowsErrorResponse::class.java)
                    _showsResponse.value = showsErrorResponse.errors[0]
                }
            }

            override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                _showsResponse.value = false.toString()
            }
        })
    }

    fun fetchTopRatedShowsFromServer() {
        ApiModule.retrofit.topRatedShows().enqueue(object : Callback<TopRatedShowsResponse> {
            override fun onResponse(call: Call<TopRatedShowsResponse>, response: Response<TopRatedShowsResponse>) {
                if (response.isSuccessful) {
                    _showsResponse.value = response.isSuccessful.toString()
                    _shows.value = response.body()?.shows
                    _showsRecyclerFullOrEmpty.value = shows.value?.isEmpty()
                } else {
                    val gson = Gson()
                    val showsErrorResponse: ShowsErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), ShowsErrorResponse::class.java)
                    _showsResponse.value = showsErrorResponse.errors[0]
                }
            }

            override fun onFailure(call: Call<TopRatedShowsResponse>, t: Throwable) {
                _showsResponse.value = false.toString()
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
                    _changePhotoResponse.value = response.body()?.user?.image_url
                } else {
                    _changePhotoResponse.value = false.toString()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _changePhotoResponse.value = false.toString()
            }
        })
    }
}