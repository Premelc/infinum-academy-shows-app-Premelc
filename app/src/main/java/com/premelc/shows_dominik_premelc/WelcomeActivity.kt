package com.premelc.shows_dominik_premelc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.premelc.shows_dominik_premelc.databinding.ActivityWelcomeBinding


class WelcomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        setContentView(binding.root)
        var username:String? = intent.extras?.getString("username")
        var retString = "Welcome, $username!"

        binding.userWelcome.text = retString

        println(binding.userWelcome.text)
    }
}