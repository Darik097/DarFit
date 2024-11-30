package com.example.darfit

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.darfit.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_nutrition -> {
                    val intent = Intent(this, NutritionActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_profile -> {
                    true
                }
                else -> false
            }
        }

        binding.bottomNavigationView.selectedItemId = R.id.menu_profile

        binding.personalDataTextView.setOnClickListener {
            val intent = Intent(this, PrivateData::class.java)
            startActivity(intent)
        }

        binding.statisticsTextView.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }

        binding.askQuestionTextView.setOnClickListener {
            val telegramUsername = "Darik097"
            val telegramUri = "https://t.me/$telegramUsername"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(telegramUri))
            startActivity(intent)
        }
    }
}
