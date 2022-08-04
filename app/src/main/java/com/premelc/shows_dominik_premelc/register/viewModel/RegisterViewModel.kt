package com.premelc.shows_dominik_premelc.register.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.premelc.shows_dominik_premelc.PASSWORD_MIN_LENGTH
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.model.RegisterErrorResponse
import com.premelc.shows_dominik_premelc.model.RegisterRequest
import com.premelc.shows_dominik_premelc.model.RegisterResponse
import com.premelc.shows_dominik_premelc.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    val repo = RegisterViewModelRepository()
    val emailValidityStringCode: LiveData<Int> = repo.getEmailValidityStringCode()
    val passwordValidityStringCode: LiveData<Int> = repo.getPasswordValidityStringCode()
    val repeatPasswordValidityStringCode: LiveData<Int> = repo.getRepeatPasswordValidityStringCode()
    val registerButtonIsEnabled: LiveData<Boolean> = repo.getRegisterButtonIsEnabled()
    val passwordsMatchStringCode: LiveData<Int> = repo.getPasswordsMatchStringCode()
    val registerResponse: LiveData<Boolean> = repo.getRegisterResponse()
    val registerErrorMessage: LiveData<String> = repo.getRegisterErrorMessage()

    fun checkIfPasswordsMatch(password: String, repeatPassword: String) {
        repo.checkIfPasswordsMatch(password, repeatPassword)
    }

    fun checkEmailValidity(emailText: String) {
        repo.checkEmailValidity(emailText)
    }

    fun checkPasswordValidity(passwordText: String) {
        repo.checkPasswordValidity(passwordText)
    }

    fun checkRepeatPasswordValidity(passwordText: String) {
        repo.checkRepeatPasswordValidity(passwordText)
    }

    fun validateRegisterData(email: String, password: String, repeatPassword: String) {
        repo.validateRegisterData(email, password, repeatPassword)
    }

    fun onRegisterButtonClicked(email: String, password: String) {
        repo.onRegisterButtonClicked(email, password)
    }
}