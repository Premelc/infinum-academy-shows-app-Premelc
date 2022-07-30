package com.premelc.shows_dominik_premelc.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.gson.Gson
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.model.LoginErrorResponse
import com.premelc.shows_dominik_premelc.model.LoginRequest
import com.premelc.shows_dominik_premelc.model.LoginResponse
import com.premelc.shows_dominik_premelc.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val PASSWORD_MIN_LENGTH = 6

class LoginViewModel : ViewModel() {
    private val _isRememberMeChecked = MutableLiveData(false)
    val isRememberMeChecked: LiveData<Boolean> = _isRememberMeChecked

    private val _emailValidityStringCode = MutableLiveData<Int>()
    val emailValidityStringCode: LiveData<Int> = _emailValidityStringCode

    private val _passwordValidityStringCode = MutableLiveData<Int>()
    val passwordValidityStringCode: LiveData<Int> = _passwordValidityStringCode

    private val _loginButtonIsEnabled = MutableLiveData<Boolean>()
    val loginButtonIsEnabled: LiveData<Boolean> = _loginButtonIsEnabled

    private val _loginResponse = MutableLiveData<Boolean>()
    val loginResponse: LiveData<Boolean> = _loginResponse

    private val _loginErrorMessage = MutableLiveData<String>()
    val loginErrorMessage: LiveData<String> = _loginErrorMessage

    private val _headerValues = MutableLiveData<Map<String , String>>()
    val headerValues: LiveData<Map<String,String>> = _headerValues

    fun initRememberMeCheckboxListener(checkbox: MaterialCheckBox) {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            _isRememberMeChecked.value = isChecked
        }
    }

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= PASSWORD_MIN_LENGTH
    }

    fun checkEmailValidity(emailText: String) {
        _emailValidityStringCode.value = when {
            validateEmail(emailText) -> null
            else -> R.string.invalidEmail
        }
    }

    fun checkPasswordValidity(passwordText: String) {
        _passwordValidityStringCode.value = when {
            validatePassword(passwordText) -> null
            else -> R.string.invalidPassword
        }
    }
    fun validateLoginData(email: String, password: String) {
        _loginButtonIsEnabled.value = validateEmail(email) && validatePassword(password)
    }

    fun onLoginButtonClicked(email: String, password: String) {
        val loginRequest = LoginRequest(
            email = email,
            password = password
        )
        ApiModule.retrofit.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _loginResponse.value = response.isSuccessful
                    _headerValues.value = mapOf(
                        SHARED_PREFERENCES_TOKEN_TYPE to "Bearer" ,
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