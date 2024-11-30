package com.example.darfit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class LevelsAdapter(
    private val levels: List<String>,
    private val onLevelSelected: (String) -> Unit
) : RecyclerView.Adapter<LevelsAdapter.LevelViewHolder>() {

    inner class LevelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val levelText: TextView = view.findViewById(R.id.levelText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_level, parent, false)
        return LevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.levelText.text = levels[position]

        holder.itemView.scaleX = 0.8f
        holder.itemView.scaleY = 0.8f
        holder.itemView.alpha = 0.8f

        holder.itemView.setOnClickListener {
            onLevelSelected(levels[position])
        }
    }

    override fun getItemCount(): Int = levels.size

    // Метод, возвращающий элемент по индексу
    fun getItem(position: Int): String {
        return levels[position]
    }
}


