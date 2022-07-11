package com.premelc.shows_dominik_premelc

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.premelc.shows_dominik_premelc.databinding.ActivityLoginBinding
import loginFunctions.validateEmail
import loginFunctions.validateLoginData
import loginFunctions.validatePassword


class LoginActivity : AppCompatActivity() {

lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var loginButton = binding.loginButton
        var emailTextView = binding.emailInput
        var passwordTextView = binding.passwordInput

        emailTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if(!validateEmail(s.toString())){
                    binding.emailInput.error = "Invalid email address"
                }
                loginButton.isEnabled = validateLoginData(emailTextView.text.toString() , passwordTextView.text.toString())
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
        }})

        passwordTextView.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable) {
                if(!validatePassword(s.toString())){
                    binding.passwordInput.error = "Invalid password"
                }
                loginButton.isEnabled = validateLoginData(emailTextView.text.toString() , passwordTextView.text.toString())
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        loginButton.setOnClickListener{
                val email = emailTextView.text.toString();

                val intent = Intent(this, WelcomeActivity::class.java)
                intent.putExtra("username" , email.substringBefore('@'))
                startActivity(intent)
        }
    }
}