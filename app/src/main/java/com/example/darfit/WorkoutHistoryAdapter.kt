package com.example.darfit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class WorkoutHistoryAdapter(private val workoutHistoryList: List<WorkoutHistoryView>) :
    RecyclerView.Adapter<WorkoutHistoryAdapter.WorkoutHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workout_history, parent, false)
        return WorkoutHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutHistoryViewHolder, position: Int) {
        val workoutHistory = workoutHistoryList[position]

        holder.timestampText.text = "Дата: ${workoutHistory.timestamp}"
        holder.workoutLengthText.text = "Продолжительность: ${workoutHistory.workoutLength} мин"
        holder.caloriesText.text = "Калории: ${workoutHistory.calories}"

        Glide.with(holder.itemView.context)
            .load(workoutHistory.imageUrl)
            .placeholder(R.drawable.img_1)
            .error(R.drawable.img_1)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = workoutHistoryList.size

    class WorkoutHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timestampText: TextView = itemView.findViewById(R.id.timestampText)
        val workoutLengthText: TextView = itemView.findViewById(R.id.workoutLengthText)
        val caloriesText: TextView = itemView.findViewById(R.id.caloriesText)
        val imageView: ImageView = itemView.findViewById(R.id.workoutImageView)
    }
}
