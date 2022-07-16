package com.premelc.shows_dominik_premelc.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.premelc.shows_dominik_premelc.databinding.ActivityLoginBinding
import com.premelc.shows_dominik_premelc.login.loginFunctions.*
import com.premelc.shows_dominik_premelc.shows.ShowsActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = binding.loginButton
        val emailTextView = binding.emailInput
        val passwordTextView = binding.passwordInput

        setupLoginValidation(emailTextView, passwordTextView, loginButton)
        setupLoginButton(this, loginButton)
    }

    private fun checkEmailRegex(emailTextView: TextView, passwordTextView: TextView, loginButton: View) {
        if (!validateEmail(emailTextView.text.toString())) {
            emailTextView.error = "Invalid email address"
        }
        loginButton.isEnabled = validateLoginData(
            emailTextView.text.toString(),
            passwordTextView.text.toString()
        )
    }

    private fun checkPassword(emailTextView: TextView, passwordTextView: TextView, loginButton: View) {
        if (!validatePassword(passwordTextView.text.toString())) {
            passwordTextView.error = "Invalid password"
        }
        loginButton.isEnabled = validateLoginData(
            emailTextView.text.toString(),
            passwordTextView.text.toString()
        )
    }

    private fun setupLoginValidation(
        emailTextView: TextView,
        passwordTextView: TextView,
        loginButton: View
    ) {

        emailTextView.doOnTextChanged { text, start, before, count -> checkEmailRegex(emailTextView, passwordTextView, loginButton) }
        passwordTextView.doOnTextChanged { text, start, before, count -> checkPassword(emailTextView, passwordTextView, loginButton) }
    }

    private fun setupLoginButton(context: Context, loginButton: View) {
        loginButton.setOnClickListener {
            val user = binding.emailInput.text.toString().substringBefore('@')
            val intent = ShowsActivity.buildShowsActivityIntent(context as Activity)
            intent.putExtra("username", user)
            context.startActivity(intent)
        }
    }
}