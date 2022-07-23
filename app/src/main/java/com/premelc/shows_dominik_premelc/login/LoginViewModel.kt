package com.premelc.shows_dominik_premelc.login

import android.view.View
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.checkbox.MaterialCheckBox

const val PASSWORD_MIN_LENGTH = 6

class LoginModelView : ViewModel() {
    private val _isRememberMeChecked = MutableLiveData(false)
    val isRememberMeChecked: LiveData<Boolean> = _isRememberMeChecked

    fun initRememberMeCheckboxListener(checkbox: MaterialCheckBox) {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            _isRememberMeChecked.value = isChecked
        }
    }

    fun setupLoginValidation(
        emailTextView: TextView,
        passwordTextView: TextView,
        loginButton: View
    ) {
        emailTextView.doOnTextChanged { text, start, before, count ->
            checkEmailRegex(
                emailTextView,
                passwordTextView,
                loginButton
            )
        }
        passwordTextView.doOnTextChanged { text, start, before, count ->
            checkPassword(
                emailTextView,
                passwordTextView,
                loginButton
            )
        }
    }

    fun checkEmailRegex(emailTextView: TextView, passwordTextView: TextView, loginButton: View) {
        if (!validateEmail(emailTextView.text.toString())) {
            emailTextView.error = "Invalid email address"
        }
        loginButton.isEnabled = validateLoginData(
            emailTextView.text.toString(),
            passwordTextView.text.toString()
        )
    }

    fun checkPassword(emailTextView: TextView, passwordTextView: TextView, loginButton: View) {
        if (!validatePassword(passwordTextView.text.toString())) {
            passwordTextView.error = "Invalid password"
        }
        loginButton.isEnabled = validateLoginData(
            emailTextView.text.toString(),
            passwordTextView.text.toString()
        )
    }

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= PASSWORD_MIN_LENGTH
    }

    private fun validateLoginData(email: String, password: String): Boolean {
        return validateEmail(email) && validatePassword(password)
    }

}