package com.premelc.shows_dominik_premelc.login

import android.widget.TextView
import androidx.core.widget.doOnTextChanged
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

    private val _loginResponse = MutableLiveData<String>()
    val loginResponse: LiveData<String> = _loginResponse

    private val _headerValues = MutableLiveData<List<String>>()
    val headerValues: LiveData<List<String>> = _headerValues

    fun initRememberMeCheckboxListener(checkbox: MaterialCheckBox) {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            _isRememberMeChecked.value = isChecked
        }
    }

    fun initLoginTextInputListeners(emailTextView: TextView, passwordTextView: TextView) {
        emailTextView.doOnTextChanged { text, start, before, count ->
            checkEmailValidity(emailTextView.text.toString())
            validateLoginData(emailTextView.text.toString(), passwordTextView.text.toString())
        }
        passwordTextView.doOnTextChanged { text, start, before, count ->
            checkPasswordValidity(passwordTextView.text.toString())
            validateLoginData(emailTextView.text.toString(), passwordTextView.text.toString())
        }
    }

    private fun checkEmailValidity(emailText: String) {
        _emailValidityStringCode.value = when {
            validateEmail(emailText) -> null
            else -> R.string.invalidEmail
        }
    }

    private fun checkPasswordValidity(passwordText: String) {
        _passwordValidityStringCode.value = when {
            validatePassword(passwordText) -> null
            else -> R.string.invalidPassword
        }
    }

    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= PASSWORD_MIN_LENGTH
    }

    private fun validateLoginData(email: String, password: String) {
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
                    _loginResponse.value = response.body()?.user?.email
                    _headerValues.value = arrayListOf(
                        "Bearer",
                        response.headers().values("access-token")[0],
                        response.headers().values("client")[0],
                        response.body()?.user?.image_url.toString()
                    )
                } else {
                    val gson = Gson()
                    val loginErrorResponse: LoginErrorResponse =
                        gson.fromJson(response.errorBody()?.string(), LoginErrorResponse::class.java)
                    _loginResponse.value = loginErrorResponse.errors[0]
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginResponse.value = false.toString()
            }
        })
    }

}