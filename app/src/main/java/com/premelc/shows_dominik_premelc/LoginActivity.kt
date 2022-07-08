package com.premelc.shows_dominik_premelc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.premelc.shows_dominik_premelc.databinding.ActivityLoginBinding
import com.premelc.shows_dominik_premelc.databinding.ActivityMainBinding


class LoginActivity : AppCompatActivity() {

lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var loginButton = binding.loginButton
        loginButton.setOnClickListener{
            var noErrors = true
            val email:String = binding.emailInput.text.toString();
            val password:String = binding.passwordInput.text.toString();
            if(!email.contains('@')){
                binding.emailInput.error = "Invalid email address"
                noErrors = false
            }
            if(password.length < 5){
                binding.passwordInput.error = "Invalid password"
                noErrors = false
            }

            if (noErrors) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.putExtra("username" , email.substringBefore('@'))
                startActivity(intent)
            }
        }
    }
}