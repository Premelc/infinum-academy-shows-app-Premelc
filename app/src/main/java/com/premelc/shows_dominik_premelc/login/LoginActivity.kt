package com.premelc.shows_dominik_premelc.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

    private fun setupLoginValidation(
        emailTextView: TextView,
        passwordTextView: TextView,
        loginButton: View
    ) {
        emailTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (!validateEmail(s.toString())) {
                    emailTextView.error = "Invalid email address"
                }
                loginButton.isEnabled = validateLoginData(
                    emailTextView.text.toString(),
                    passwordTextView.text.toString()
                )
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
        passwordTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (!validatePassword(s.toString())) {
                    passwordTextView.error = "Invalid password"
                }
                loginButton.isEnabled = validateLoginData(
                    emailTextView.text.toString(),
                    passwordTextView.text.toString()
                )
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
    }

    private fun setupLoginButton(context: Context, loginButton: View) {
        loginButton.setOnClickListener {
            /*
            val intent = Intent(ACTION_SEND).apply {
                putExtra("username", emailTextView.text.toString().substringBefore('@'))
                type = "text/plain"
            }
            */
            val user = binding.emailInput.text.toString().substringBefore('@')
            val intent = ShowsActivity.buildShowsActivityIntent(context as Activity)
            intent.putExtra("username", user)
            context.startActivity(intent)
        }
    }
}