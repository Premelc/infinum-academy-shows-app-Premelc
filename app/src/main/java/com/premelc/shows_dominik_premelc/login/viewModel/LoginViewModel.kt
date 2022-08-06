package com.premelc.shows_dominik_premelc.login.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.premelc.shows_dominik_premelc.PASSWORD_MIN_LENGTH
import com.premelc.shows_dominik_premelc.R

class LoginViewModel : ViewModel() {

    private val _isRememberMeChecked = MutableLiveData(false)
    val isRememberMeChecked: LiveData<Boolean> = _isRememberMeChecked

    private val _emailValidityStringCode = MutableLiveData<Int>()
    val emailValidityStringCode: LiveData<Int> = _emailValidityStringCode

    private val _passwordValidityStringCode = MutableLiveData<Int>()
    val passwordValidityStringCode: LiveData<Int> = _passwordValidityStringCode

    private val _loginButtonIsEnabled = MutableLiveData<Boolean>()
    val loginButtonIsEnabled: LiveData<Boolean> = _loginButtonIsEnabled

    private val repo = LoginViewModelRepository()
    val loginResponse: LiveData<Boolean> = repo.getLoginResponse()
    val loginErrorMessage: LiveData<String> = repo.getLoginErrorMessage()
    val headerValues: LiveData<Map<String, String>> = repo.getHeaderValues()

    fun initRememberMeCheckboxListener(isChecked: Boolean) {
        _isRememberMeChecked.value = isChecked
    }

    private fun validateEmail(email: String): Boolean {
        return (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty())
    }

    private fun validatePassword(password: String): Boolean {
        return (password.length >= PASSWORD_MIN_LENGTH || password.isEmpty())
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
        repo.loginUser(email , password)
    }

}