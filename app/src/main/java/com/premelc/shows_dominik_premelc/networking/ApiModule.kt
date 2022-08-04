package com.premelc.shows_dominik_premelc.networking

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_ACCESS_TOKEN
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_CLIENT
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_EMAIL
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_TOKEN_TYPE
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {
    private const val BASE_URL = "https://tv-shows.infinum.academy/"
    lateinit var retrofit: ShowsApiService

    fun initRetrofit(context: Context, header: Map<String, String>) {

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        val okhttp = OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor.Builder(context).build())

        if (header.isNotEmpty()) {
            okhttp.addInterceptor(Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("Accept", "application/json")
                builder.header("access-token", header[SHARED_PREFERENCES_ACCESS_TOKEN].toString())
                builder.header("client", header[SHARED_PREFERENCES_CLIENT].toString())
                builder.header("token-type", header[SHARED_PREFERENCES_TOKEN_TYPE].toString())
                builder.header("uid", header[SHARED_PREFERENCES_EMAIL].toString())
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
