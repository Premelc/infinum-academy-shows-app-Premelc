package com.premelc.shows_dominik_premelc.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.premelc.shows_dominik_premelc.databinding.ActivityWelcomeBinding
import com.premelc.shows_dominik_premelc.shows.ShowsActivity


class WelcomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username: String = "Welcome, " + intent.extras?.getString("username")
        binding.userWelcome.text = username

        initElipseButton()
    }

    private fun initElipseButton(){
        //intent
        binding.elipseImg.setOnClickListener{
            val intent = Intent(this , ShowsActivity::class.java)
            startActivity(intent)
        }
    }
}