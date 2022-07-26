package com.premelc.shows_dominik_premelc.login

import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.checkbox.MaterialCheckBox
import com.premelc.shows_dominik_premelc.R

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

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= PASSWORD_MIN_LENGTH
    }

    fun validateLoginData(email: String, password: String) {
        _loginButtonIsEnabled.value = validateEmail(email) && validatePassword(password)
    }

}