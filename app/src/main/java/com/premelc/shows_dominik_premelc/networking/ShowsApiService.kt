package com.premelc.shows_dominik_premelc.networking

import com.premelc.shows_dominik_premelc.model.LoginRequest
import com.premelc.shows_dominik_premelc.model.LoginResponse
import com.premelc.shows_dominik_premelc.model.RegisterRequest
import com.premelc.shows_dominik_premelc.model.RegisterResponse
import com.premelc.shows_dominik_premelc.model.ShowDetailsRequest
import com.premelc.shows_dominik_premelc.model.ShowDetailsResponse
import com.premelc.shows_dominik_premelc.model.ShowsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// TODO define all functions required to communicate with the server
interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/shows")
    fun shows(): Call<ShowsResponse>

    @GET("/shows/{id}")
    fun specificShow(@Path("id") id: String): Call<ShowDetailsResponse>
}