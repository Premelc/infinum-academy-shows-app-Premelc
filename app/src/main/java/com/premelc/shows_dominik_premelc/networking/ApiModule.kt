package com.premelc.shows_dominik_premelc.networking

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.premelc.shows_dominik_premelc.model.ChangePhotoRequest
import com.premelc.shows_dominik_premelc.model.LoginResponse
import java.io.File
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {
    private const val BASE_URL = "https://tv-shows.infinum.academy/"
    lateinit var retrofit: ShowsApiService
    val MEDIA_TYPE_JPG = "image/jpg".toMediaType()

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

   fun initRetrofitImageUpload(context:Context , header: List<String>, filePath: String, email: String)  {

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("image=@", "avatar.jpg", File(filePath).asRequestBody(MEDIA_TYPE_JPG)).build()

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
                builder.put(requestBody)
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

        retrofit.changePhoto(ChangePhotoRequest(email)).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                println(response.body()?.user?.image_url)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }
}
