package com.premelc.shows_dominik_premelc.register.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.premelc.shows_dominik_premelc.model.RegisterErrorResponse
import com.premelc.shows_dominik_premelc.model.RegisterRequest
import com.premelc.shows_dominik_premelc.model.RegisterResponse
import com.premelc.shows_dominik_premelc.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModelRepository {

    private val _registerErrorMessage = MutableLiveData<String>()
    private val _registerResponse = MutableLiveData<Boolean>()

    fun getRegisterResponse() = _registerResponse
    fun getRegisterErrorMessage() = _registerErrorMessage

    fun RegisterUser(email: String, password: String) {
        val registerRequest = RegisterRequest(
            email = email,
            password = password,
            passwordConfirmation = password
        )
        ApiModule.retrofit.register(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    _registerResponse.value = response.isSuccessful
                } else {
                    val gson = Gson()
                    val registerErrorResponse: RegisterErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), RegisterErrorResponse::class.java)
                    _registerErrorMessage.value = registerErrorResponse.errors.first()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registerResponse.value = false
            }
        })
    }

}