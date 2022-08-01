package com.premelc.shows_dominik_premelc.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.login.PASSWORD_MIN_LENGTH
import com.premelc.shows_dominik_premelc.model.RegisterErrorResponse
import com.premelc.shows_dominik_premelc.model.RegisterRequest
import com.premelc.shows_dominik_premelc.model.RegisterResponse
import com.premelc.shows_dominik_premelc.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _emailValidityStringCode = MutableLiveData<Int>()
    val emailValidityStringCode: LiveData<Int> = _emailValidityStringCode

    private val _passwordValidityStringCode = MutableLiveData<Int>()
    val passwordValidityStringCode: LiveData<Int> = _passwordValidityStringCode

    private val _repeatPasswordValidityStringCode = MutableLiveData<Int>()
    val repeatPasswordValidityStringCode: LiveData<Int> = _repeatPasswordValidityStringCode

    private val _registerButtonIsEnabled = MutableLiveData<Boolean>()
    val registerButtonIsEnabled: LiveData<Boolean> = _registerButtonIsEnabled

    private val _passwordsMatchStringCode = MutableLiveData<Int>()
    val passwordsMatchStringCode: LiveData<Int> = _passwordsMatchStringCode

    private val _registerResponse = MutableLiveData<Boolean>()
    val registerResponse: LiveData<Boolean> = _registerResponse

    private val _registerErrorMessage = MutableLiveData<String>()
    val registerErrorMessage: LiveData<String> = _registerErrorMessage

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= PASSWORD_MIN_LENGTH
    }

    fun checkIfPasswordsMatch(password: String, repeatPassword: String) {
        _passwordsMatchStringCode.value = if (password == repeatPassword) null else R.string.passwords_dont_match
    }

    fun checkEmailValidity(emailText: String) {
        _emailValidityStringCode.value = if (validateEmail(emailText)) null else R.string.invalidEmail
    }

    fun checkPasswordValidity(passwordText: String) {
        _passwordValidityStringCode.value = if (validatePassword(passwordText)) null else R.string.invalidPassword
    }

    fun checkRepeatPasswordValidity(passwordText: String) {
        _repeatPasswordValidityStringCode.value = if (validatePassword(passwordText)) null else R.string.invalidPassword
    }

    fun validateRegisterData(email: String, password: String, repeatPassword: String) {
        _registerButtonIsEnabled.value = validateEmail(email) && validatePassword(password) && password == repeatPassword
    }

    fun onRegisterButtonClicked(email: String, password: String) {
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