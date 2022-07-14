package com.premelc.shows_dominik_premelc.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.premelc.shows_dominik_premelc.databinding.ActivityWelcomeBinding
import com.premelc.shows_dominik_premelc.shows.ShowsActivity.Companion.buildShowsActivityIntent

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user = intent.extras?.getString("username")

        val username: String = "Welcome, " + user
        binding.userWelcome.text = username

        initElipseButton(user)
    }

    private fun initElipseButton(user: String?) {
        binding.elipseImg.setOnClickListener {
            val intent = buildShowsActivityIntent(this)
            intent.putExtra("username" ,user)
            startActivity(intent)
        }
    }
}