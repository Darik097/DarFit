package com.example.darfit

import TrainingTypeAdapter
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.darfit.api.ApiMethods
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var strengthButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var userName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        strengthButton = findViewById(R.id.strengthButton)
        recyclerView = findViewById(R.id.trainRecyclerView)
        userName = findViewById(R.id.userName)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    true
                }
                R.id.menu_nutrition -> {
                    val intent = Intent(this, NutritionActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val fullName = sharedPreferences.getString("user_name", "Гость") // Значение по умолчанию "Гость"
        userName.text = fullName

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchWorkouts()

        strengthButton.setOnClickListener {
            fetchWorkouts()
        }

    }

    private fun fetchWorkouts() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = ApiMethods.getWorkouts()

            withContext(Dispatchers.Main) {
                result.onSuccess { workouts ->
                    updateRecyclerView(workouts)
                }.onFailure { error ->
                    Log.e("MainActivity", "Error fetching workouts: ${error.message}")
                }
            }
        }
    }

    private fun updateRecyclerView(data: List<Workout>) {
        recyclerView.adapter = TrainingTypeAdapter(data) { selectedWorkout ->
            Log.d("MainActivity", "Workout selected: ${selectedWorkout.name}")

            val intent = Intent(this, WorkoutDetailActivity::class.java).apply {
                putExtra("WORKOUT_ID", selectedWorkout.id)
            }
            startActivity(intent)
        }

        strengthButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorActiveFirst)
    }
}
