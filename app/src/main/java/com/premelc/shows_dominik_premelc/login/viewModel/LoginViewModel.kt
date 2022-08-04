package com.premelc.shows_dominik_premelc.login.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val repo = LoginViewModelRepository()
    val isRememberMeChecked: LiveData<Boolean> = repo.getIsRememberMeChecked()
    val emailValidityStringCode: LiveData<Int> = repo.getEmailValidityStringCode()
    val passwordValidityStringCode: LiveData<Int> = repo.getPasswordValidityStringCode()
    val loginButtonIsEnabled: LiveData<Boolean> = repo.getLoginButtonIsEnabled()
    val loginResponse: LiveData<Boolean> = repo.getLoginResponse()
    val loginErrorMessage: LiveData<String> = repo.getLoginErrorMessage()
    val headerValues: LiveData<Map<String, String>> = repo.getHeaderValues()

    fun initRememberMeCheckboxListener(isChecked: Boolean) {
        repo.initRememberMeCheckboxListener(isChecked)
    }

    fun onLoginButtonClicked(email: String, password: String) {
        repo.onLoginButtonClicked(email, password)
    }

    fun checkEmailValidity(emailText: String) {
        repo.checkEmailValidity(emailText)
    }

    fun checkPasswordValidity(passwordText: String) {
        repo.checkPasswordValidity(passwordText)
    }

    fun validateLoginData(email: String, password: String) {
        repo.validateLoginData(email, password)
    }

}