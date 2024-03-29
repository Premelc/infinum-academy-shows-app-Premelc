package com.premelc.shows_dominik_premelc.login.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_ACCESS_TOKEN
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_CLIENT
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_PFP_URL
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_TOKEN_TYPE
import com.premelc.shows_dominik_premelc.model.LoginErrorResponse
import com.premelc.shows_dominik_premelc.model.LoginRequest
import com.premelc.shows_dominik_premelc.model.LoginResponse
import com.premelc.shows_dominik_premelc.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModelRepository {
    private val _loginResponse = MutableLiveData<Boolean>()
    private val _loginErrorMessage = MutableLiveData<String>()
    private val _headerValues = MutableLiveData<Map<String, String>>()

    fun getLoginResponse() = _loginResponse
    fun getLoginErrorMessage() = _loginErrorMessage
    fun getHeaderValues() = _headerValues

    fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(
            email = email,
            password = password
        )
        ApiModule.retrofit.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _loginResponse.value = response.isSuccessful
                    _headerValues.value = mapOf(
                        SHARED_PREFERENCES_TOKEN_TYPE to "Bearer",
                        SHARED_PREFERENCES_ACCESS_TOKEN to response.headers().values("access-token")[0],
                        SHARED_PREFERENCES_CLIENT to response.headers().values("client")[0],
                        SHARED_PREFERENCES_PFP_URL to response.body()?.user?.image_url.toString()
                    )
                } else {
                    val gson = Gson()
                    val loginErrorResponse: LoginErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), LoginErrorResponse::class.java)
                    _loginErrorMessage.value = loginErrorResponse.errors.first()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginResponse.value = false
            }
        })
    }
}