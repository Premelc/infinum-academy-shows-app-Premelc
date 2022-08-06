package com.premelc.shows_dominik_premelc.register.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.premelc.shows_dominik_premelc.PASSWORD_MIN_LENGTH
import com.premelc.shows_dominik_premelc.R

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

    private val repo = RegisterViewModelRepository()
    val registerResponse: LiveData<Boolean> = repo.getRegisterResponse()
    val registerErrorMessage: LiveData<String> = repo.getRegisterErrorMessage()

    private fun validateEmail(email: String): Boolean {
        return (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty())
    }

    private fun validatePassword(password: String): Boolean {
        return (password.length >= PASSWORD_MIN_LENGTH || password.isEmpty())
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
        repo.RegisterUser(email, password)
    }
}