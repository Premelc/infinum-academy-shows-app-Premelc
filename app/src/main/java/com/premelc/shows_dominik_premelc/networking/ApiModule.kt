package com.premelc.shows_dominik_premelc.networking

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {
    private const val BASE_URL = "https://tv-shows.infinum.academy/"
    lateinit var retrofit: ShowsApiService

    fun initRetrofit(context: Context, header: List<String>) {

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        val okhttp = OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor.Builder(context).build())

        if (header.isNotEmpty()) {
            okhttp.addInterceptor(Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("Accept", "application/json")
                builder.header("access-token", header[1])
                builder.header("client", header[2])
                builder.header("token-type", header[0])
                builder.header("uid", header[3])
                builder.header("Content-Type", "application/json")
                return@Interceptor chain.proceed(builder.build())
            }).build()
        } else {
            okhttp.build()
        }

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okhttp.build())
            .build()
            .create(ShowsApiService::class.java)
    }

}
