package com.premelc.shows_dominik_premelc.networking

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {
    private const val BASE_URL = "https://tv-shows.infinum.academy/"
    lateinit var retrofit: ShowsApiService

    fun initRetrofit(context: Context) {

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        val okhttp = OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okhttp)
            .build()
            .create(ShowsApiService::class.java)
    }
}