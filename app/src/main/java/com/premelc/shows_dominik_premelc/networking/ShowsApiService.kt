package com.premelc.shows_dominik_premelc.networking

import com.premelc.shows_dominik_premelc.model.RegisterRequest
import com.premelc.shows_dominik_premelc.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// TODO define all functions required to communicate with the server
interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

}