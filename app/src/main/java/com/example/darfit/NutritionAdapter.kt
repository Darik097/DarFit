package com.example.darfit

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.darfit.R

class NutritionAdapter(private val nutritionList: List<Nutrition>) :
    RecyclerView.Adapter<NutritionAdapter.NutritionViewHolder>() {

    class NutritionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeName: TextView = view.findViewById(R.id.nutritionName)
        val recipeImage: ImageView = view.findViewById(R.id.nutritionImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nutrition, parent, false)
        return NutritionViewHolder(view)
    }

    override fun onBindViewHolder(holder: NutritionViewHolder, position: Int) {
        val item = nutritionList[position]
        holder.recipeName.text = item.recipeName

        // Используем Glide для загрузки изображения
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)  // Загружаем изображение по URL
            .placeholder(R.drawable.img_1)  // Укажите свой ресурс-заглушку
            .into(holder.recipeImage)

        // Обработка клика
        holder.itemView.setOnClickListener {
            // Создаем Intent для перехода на новую активность
            val intent = Intent(holder.itemView.context, NutritionDetailActivity::class.java)

            // Передаем все данные через Intent
            intent.putExtra("id", item.id)
            intent.putExtra("recipeName", item.recipeName)
            intent.putExtra("calories", item.calories)
            intent.putExtra("protein", item.protein)
            intent.putExtra("fat", item.fat)
            intent.putExtra("carbohydrates", item.carbohydrates)
            intent.putExtra("recipeText", item.recipeText)
            intent.putExtra("imageUrl", item.imageUrl)

            // Запускаем активность
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = nutritionList.size
}




