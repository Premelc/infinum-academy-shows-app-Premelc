package com.premelc.shows_dominik_premelc.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.premelc.shows_dominik_premelc.ShowsObject.showsList
import com.premelc.shows_dominik_premelc.model.LoginErrorResponse
import com.premelc.shows_dominik_premelc.model.Show
import com.premelc.shows_dominik_premelc.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModel : ViewModel() {

    private val _shows = MutableLiveData<List<Show>>(emptyList())
    val shows: LiveData<List<Show>> = _shows

    private val _showsRecyclerFullOrEmpty = MutableLiveData<Boolean>()
    val showsRecyclerFullOrEmpty: LiveData<Boolean> = _showsRecyclerFullOrEmpty

    private val _showsResponse = MutableLiveData<String>()
    val showsResponse: LiveData<String> = _showsResponse

    init {
       fetchShowsFromServer()
    }

    fun fetchShowsFromServer() {
        var showList = emptyList<Show>()
            ApiModule.retrofit.shows().enqueue(object:Callback<ShowsResponse>{
                override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                    if(response.isSuccessful){
                        _showsResponse.value = response.isSuccessful.toString()
                        for (item in response.body()?.shows!!){
                            showList = showList + Show(item.id , item.title , item.description , emptyList() ,item.imageUrl)
                        }
                        _shows.value = showList
                        _showsRecyclerFullOrEmpty.value = shows.value?.isEmpty()
                        for (item in shows.value!!){
                            println(item.name)
                        }
                    }else{
                        val gson = Gson()
                        val showsErrorResponse: ShowsErrorResponse = gson.fromJson(response.errorBody()?.string() , ShowsErrorResponse::class.java)
                        _showsResponse.value = showsErrorResponse.errors[0]
                    }
                }
                override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                    _showsResponse.value = false.toString()
                }
            })

    }
}