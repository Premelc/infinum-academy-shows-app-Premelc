package com.premelc.shows_dominik_premelc.showDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.premelc.shows_dominik_premelc.model.Review
import com.premelc.shows_dominik_premelc.model.Show
import com.premelc.shows_dominik_premelc.model.ShowDetailsErrorResponse
import com.premelc.shows_dominik_premelc.model.ShowDetailsResponse
import com.premelc.shows_dominik_premelc.model.ShowsErrorResponse
import com.premelc.shows_dominik_premelc.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel : ViewModel() {
    private var _show = MutableLiveData<Show>()
    val show: LiveData<Show> = _show

    private var _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    private var _rating = MutableLiveData<Float>()
    val rating: LiveData<Float> = _rating

    private var _reviewsRecyclerFullOrEmpty = MutableLiveData<Boolean>()
    val reviewsRecyclerFullOrEmpty: LiveData<Boolean> = _reviewsRecyclerFullOrEmpty

    private var _id = MutableLiveData<String>()
    val id: LiveData<String> = _id

    private var _showsDetailResponse = MutableLiveData<String>()
    val showsDetailResponse: LiveData<String> = _showsDetailResponse

    fun initDetails(id: String) {
        ApiModule.retrofit.specificShow(id).enqueue(object:Callback<ShowDetailsResponse>{
            override fun onResponse(call: Call<ShowDetailsResponse>, response: Response<ShowDetailsResponse>) {
                if(response.isSuccessful){
                    _show.value = response.body()?.show
                    println("AVERAGE: " + _show.value?.average_rating)
                    println("COUNT: " + _show.value?.no_of_reviews)
                    println("AVERAGE: " + response.body()?.show?.average_rating)
                    println("COUNT: " + response.body()?.show?.no_of_reviews)
                    println(response.body()?.show)
                    _showsDetailResponse.value = response.isSuccessful.toString()
                }else{
                    val gson = Gson()
                    val showDetailsErrorResponse: ShowDetailsErrorResponse = gson.fromJson(response.errorBody()?.string() , ShowDetailsErrorResponse::class.java)
                    _showsDetailResponse.value = showDetailsErrorResponse.errors[0]
                }
            }
            override fun onFailure(call: Call<ShowDetailsResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setShow(show: Show) {
        _show.value = show
    }

    fun addReview(review: Review) {
        _reviews.value = reviews.value?.plus(review)
    }
}