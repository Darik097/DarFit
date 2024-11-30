package com.example.darfit

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.darfit.api.ApiMethods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NutritionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var nutritionAdapter: NutritionAdapter
    private var nutritionList: List<Nutrition> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        recyclerView = findViewById(R.id.nutritionRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchNutritionData()

        val bottomNavigationView = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setSelectedItemId(R.id.menu_nutrition)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_nutrition -> {
                    // На экране "Питание", ничего не делаем
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
    }

    // Функция для получения данных о питании
    private fun fetchNutritionData() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = ApiMethods.getNutritionData()

            runOnUiThread {
                if (result.isSuccess) {
                    nutritionList = result.getOrNull() ?: mutableListOf()
                    nutritionAdapter = NutritionAdapter(nutritionList)
                    recyclerView.adapter = nutritionAdapter
                } else {
                    Toast.makeText(
                        this@NutritionActivity,
                        "Ошибка при загрузке данных",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
