package com.example.darfit

import TrainingTypeAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.darfit.api.ApiMethods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.absoluteValue

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userName: TextView
    private lateinit var dailyWorkoutLayout: View
    private lateinit var workoutNameTextView: TextView
    private lateinit var workoutImageView: ImageView
    private var workouts: List<Workout> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.trainRecyclerView)
        userName = view.findViewById(R.id.userName)
        dailyWorkoutLayout = view.findViewById(R.id.linearLayout)
        workoutNameTextView = view.findViewById(R.id.overlayText)
        workoutImageView = view.findViewById(R.id.imageView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val fullName = sharedPreferences.getString("user_name", "Гость")
        userName.text = fullName

        fetchWorkouts()

        return view
    }

    private fun fetchWorkouts() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val result = ApiMethods.getWorkouts()

            withContext(Dispatchers.Main) {
                if (isAdded) {
                    result.onSuccess { fetchedWorkouts ->
                        workouts = fetchedWorkouts
                        updateRecyclerView(workouts)
                        selectDailyWorkout()
                    }.onFailure { error ->
                        Log.e("HomeFragment", "Error fetching workouts: ${error.message}")
                    }
                } else {
                    Log.w("HomeFragment", "Fragment is not attached to context, skipping UI updates")
                }
            }
        }
    }

    private fun updateRecyclerView(data: List<Workout>) {
        if (!isAdded) return

        recyclerView.adapter = TrainingTypeAdapter(data) { selectedWorkout ->
            navigateToWorkoutDetail(selectedWorkout.id)
        }
    }

    private fun selectDailyWorkout() {
        if (workouts.isEmpty()) return

        val sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val completedWorkouts = sharedPreferences.getStringSet("completed_workouts", mutableSetOf()) ?: mutableSetOf()
        val currentDate = getCurrentDate()

        val lastUpdatedDate = sharedPreferences.getString("last_updated_date", "")


        if (lastUpdatedDate == currentDate) {
            val workoutId = sharedPreferences.getInt("daily_workout_id", -1)
            val selectedWorkout = workouts.find { it.id == workoutId }
            selectedWorkout?.let {
                displayDailyWorkout(it)
            }
            Log.d("HomeFragment", "Workout already selected for today. Showing the same workout.")
            return
        }

        val uncompletedWorkouts = workouts.filter { it.id.toString() !in completedWorkouts }

        val selectedWorkout: Workout

        if (uncompletedWorkouts.isNotEmpty()) {
            // Выбираем новую тренировку (по кругу)
            selectedWorkout = uncompletedWorkouts[(currentDate.hashCode().absoluteValue) % uncompletedWorkouts.size]
            completedWorkouts.add(selectedWorkout.id.toString())
        } else {
            // Если все тренировки пройдены, очищаем список завершенных тренировок и начинаем новый круг
            completedWorkouts.clear()
            selectedWorkout = workouts[(currentDate.hashCode().absoluteValue) % workouts.size]
            completedWorkouts.add(selectedWorkout.id.toString())
        }

        // Сохраняем новое состояние
        sharedPreferences.edit()
            .putStringSet("completed_workouts", completedWorkouts)
            .putString("last_updated_date", currentDate)
            .putInt("daily_workout_id", selectedWorkout.id)
            .apply()

        displayDailyWorkout(selectedWorkout)
    }





    private fun displayDailyWorkout(workout: Workout) {
        workoutNameTextView.text = workout.name

        Glide.with(this)
            .load(workout.imageUrl)
            .placeholder(R.drawable.img_1)
            .error(R.drawable.img_1)
            .into(workoutImageView)


        dailyWorkoutLayout.setOnClickListener {
            navigateToWorkoutDetail(workout.id)
        }
    }

    private fun navigateToWorkoutDetail(workoutId: Int) {
        val intent = Intent(requireContext(), WorkoutDetailActivity::class.java).apply {
            putExtra("WORKOUT_ID", workoutId)
        }
        startActivity(intent)
    }

    private fun getCurrentDate(): String {
        val dateFormatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return dateFormatter.format(java.util.Date())
    }


}

