package com.premelc.shows_dominik_premelc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.premelc.shows_dominik_premelc.databinding.ActivityLoginBinding
import loginFunctions.setupLoginButton
import loginFunctions.setupLoginValidation

class LoginActivity : AppCompatActivity() {

private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = binding.loginButton
        val emailTextView = binding.emailInput
        val passwordTextView = binding.passwordInput

        setupLoginValidation(emailTextView , passwordTextView, loginButton)
        setupLoginButton(this ,loginButton,emailTextView)
    }
}