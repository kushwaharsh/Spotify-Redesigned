package com.example.spotify.activity.home

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.spotify.R
import com.example.spotify.databinding.ActivityEventDetailsBinding

class EventDetailsActivity : AppCompatActivity(){

        private lateinit var binding : ActivityEventDetailsBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            //enableEdgeToEdge()
            binding = ActivityEventDetailsBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setUpStatusBar()
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(0, 0, 0, systemBars.bottom)
                insets
            }

            initView()
            listener()


        }

        private fun listener() {

            binding.backBtn.setOnClickListener{
                finish()
            }
        }

        private fun initView() {
        }

        private fun setUpStatusBar(){
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = Color.TRANSPARENT

            // Adjust status bar icon color based on the background
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
                // Set light status bar icons if needed
                windowInsetsController.isAppearanceLightStatusBars = false
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        )
            }
        }

    }