package com.example.spotify.activity.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spotify.R
import com.example.spotify.activity.auth.RegisterOrSignInActivity
import com.example.spotify.databinding.ActivityChoosemodeBinding

class ChooseModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChoosemodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoosemodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.continueBtn.setOnClickListener {
            val intent = Intent(this, RegisterOrSignInActivity::class.java)
            startActivity(intent)
        }
    }
}