package com.premelc.shows_dominik_premelc.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.checkbox.MaterialCheckBox
import com.premelc.shows_dominik_premelc.R

const val PASSWORD_MIN_LENGTH = 6
const val sharedPreferencesFileName = "SHOWS"
const val sharedPreferencesRememberMe = "REMEMBER_ME"
const val sharedPreferencesEmail = "EMAIL"

class LoginModelView : ViewModel() {
    private val _isRememberMeChecked = MutableLiveData(false)
    val isRememberMeChecked: LiveData<Boolean> = _isRememberMeChecked

    fun initRememberMeCheckboxListener(checkbox: MaterialCheckBox) {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            _isRememberMeChecked.value = isChecked
        }
    }

    fun checkEmailValidity(emailText: String): Int? {
        return if (!validateEmail(emailText)) R.string.invalidEmail
        else null
    }

    fun checkPasswordValidity(passwordText: String): Int? {
        return if (!validatePassword(passwordText)) R.string.invalidPassword
        else null
    }

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= PASSWORD_MIN_LENGTH
    }

    fun validateLoginData(email: String, password: String): Boolean {
        return validateEmail(email) && validatePassword(password)
    }

}