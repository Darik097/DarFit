package com.example.darfit

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.appcompat.app.AppCompatActivity

class NutritionDetailActivity : AppCompatActivity() {

    private lateinit var recipeNameTextView: TextView
    private lateinit var recipeImageView: ImageView
    private lateinit var recipeTextView: TextView
    private lateinit var caloriesTextView: TextView
    private lateinit var proteinTextView: TextView
    private lateinit var fatTextView: TextView
    private lateinit var carbohydratesTextView: TextView
    private lateinit var goLoginImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition_detail)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Инициализация элементов UI
        recipeNameTextView = findViewById(R.id.recipeName)
        recipeImageView = findViewById(R.id.recipeImage)
        recipeTextView = findViewById(R.id.recipeText)
        caloriesTextView = findViewById(R.id.calories)
        proteinTextView = findViewById(R.id.protein)
        fatTextView = findViewById(R.id.fat)
        carbohydratesTextView = findViewById(R.id.carbohydrates)
        goLoginImageView = findViewById(R.id.goLogin)

        // Получаем данные из Intent
        val recipeName = intent.getStringExtra("recipeName")
        val imageUrl = intent.getStringExtra("imageUrl")
        val recipeText = intent.getStringExtra("recipeText")
        val calories = intent.getIntExtra("calories", 0)
        val protein = intent.getDoubleExtra("protein", 0.0)
        val fat = intent.getDoubleExtra("fat", 0.0)
        val carbohydrates = intent.getDoubleExtra("carbohydrates", 0.0)

        // Устанавливаем данные в элементы UI
        recipeNameTextView.text = recipeName
        recipeTextView.text = recipeText
        caloriesTextView.text = "КАЛОРИИ: $calories"
        proteinTextView.text = "БЕЛКИ: $protein g"
        fatTextView.text = "ЖИРЫ: $fat g"
        carbohydratesTextView.text = "УГЛЕВОДЫ: $carbohydrates g"

        // Загружаем изображение с помощью Glide
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.img_1)
            .into(recipeImageView)

        // Обработчик нажатия на ImageView
        goLoginImageView.setOnClickListener {
            // Возвращаемся на ActivityForFragment и заменяем фрагмент на NutritionFragment
            val intent = Intent(this, ActivityForFragment::class.java)
            startActivity(intent)
        }
    }
}
