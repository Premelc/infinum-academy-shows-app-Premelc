package com.premelc.shows_dominik_premelc.networking

import com.premelc.shows_dominik_premelc.model.LoginRequest
import com.premelc.shows_dominik_premelc.model.LoginResponse
import com.premelc.shows_dominik_premelc.model.PostReviewRequest
import com.premelc.shows_dominik_premelc.model.PostReviewResponse
import com.premelc.shows_dominik_premelc.model.RegisterRequest
import com.premelc.shows_dominik_premelc.model.RegisterResponse
import com.premelc.shows_dominik_premelc.model.ReviewsResponse
import com.premelc.shows_dominik_premelc.model.ShowDetailsResponse
import com.premelc.shows_dominik_premelc.model.ShowsResponse
import com.premelc.shows_dominik_premelc.model.TopRatedShowsResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/shows")
    fun shows(): Call<ShowsResponse>

    @GET("/shows/{id}")
    fun specificShow(@Path("id") id: String): Call<ShowDetailsResponse>

    @GET("shows/{show_id}/reviews")
    fun showReviews(@Path("show_id") show_id: Int): Call<ReviewsResponse>

    @POST("reviews")
    fun postReview(@Body request: PostReviewRequest): Call<PostReviewResponse>

    @PUT("/users")
    fun changePhoto(@Body request: MultipartBody): Call<LoginResponse>

    @GET("shows/top_rated")
    fun topRatedShows(): Call<TopRatedShowsResponse>

    @GET("users/me")
    fun getMe(): Call<LoginResponse>
}