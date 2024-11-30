package com.example.darfit

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.darfit.api.ApiMethods
import com.example.darfit.api.ApiMethods.recordWeightStatistic
import com.example.darfit.api.ApiMethods.recordWorkoutHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkoutDetailActivity : AppCompatActivity() {

    private lateinit var exerciseRecyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var buttonStartFinish: Button
    private lateinit var chronometer: Chronometer
    private var isWorkoutStarted = false
    private var workoutId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_detail)

        workoutId = intent.getIntExtra("WORKOUT_ID", -1)
        if (workoutId == -1) {
            Log.e("WorkoutDetailActivity", "Invalid workout ID")
            finish()
        }

        exerciseRecyclerView = findViewById(R.id.exerciseRecyclerView)
        buttonStartFinish = findViewById(R.id.buttonRegister)
        chronometer = findViewById(R.id.chronometer)

        exerciseRecyclerView.layoutManager = LinearLayoutManager(this)

        buttonStartFinish.setOnClickListener {
            if (!isWorkoutStarted) {
                startWorkout()
            } else {
                showWeightInputDialog()
            }
        }

        fetchExercise(workoutId)
    }

    private fun fetchExercise(workoutId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = ApiMethods.getExerciseForWorkout(workoutId)

                withContext(Dispatchers.Main) {
                    result.onSuccess { exercises ->
                        displayExercise(exercises)
                    }.onFailure { error ->
                        Log.e("WorkoutDetailActivity", "Error fetching exercises: ${error.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("WorkoutDetailActivity", "Error fetching exercises: ${e.message}")
            }
        }
    }

    private fun displayExercise(exercises: List<Exercise>) {
        exerciseAdapter = ExerciseAdapter(exercises)
        exerciseRecyclerView.adapter = exerciseAdapter
    }

    private fun startWorkout() {
        isWorkoutStarted = true
        buttonStartFinish.text = "ЗАВЕРШИТЬ"
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.visibility = View.VISIBLE
        chronometer.start()
        Toast.makeText(this, "Тренировка началась!", Toast.LENGTH_SHORT).show()
    }

    private fun showWeightInputDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_weight_input, null)
        val weightInput = dialogView.findViewById<EditText>(R.id.dialogWeightInput)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val saveButton = dialogView.findViewById<Button>(R.id.buttonSave)
        saveButton.setOnClickListener {
            val weight = weightInput.text.toString().toDoubleOrNull()
            if (weight != null) {
                finishWorkout(weight)
                dialog.dismiss() // Закрыть диалог после сохранения
            } else {
                Toast.makeText(this, "Введите корректный вес!", Toast.LENGTH_SHORT).show()
            }
        }

        val backButton = dialogView.findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun finishWorkout(weight: Double) {
        chronometer.stop()
        val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
        val seconds = (elapsedMillis / 1000).toInt()

        val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val completedWorkouts = sharedPreferences.getStringSet("completed_workouts", mutableSetOf()) ?: mutableSetOf()
        completedWorkouts.add(workoutId.toString())

        sharedPreferences.edit()
            .putStringSet("completed_workouts", completedWorkouts)
            .apply()

        Toast.makeText(
            this,
            "Тренировка завершена!\nВремя: ${seconds}с\nВес: $weight кг",
            Toast.LENGTH_LONG
        ).show()

        val userId = sharedPreferences.getInt("user_id", -1)
        if (userId != -1 && userId != 0) {
            val timestamp = System.currentTimeMillis()

            CoroutineScope(Dispatchers.IO).launch {
                val result = recordWorkoutHistory(userId, workoutId, timestamp, seconds)
                if (result.isSuccess) {
                    Log.d("WorkoutDetailActivity", "Тренировка успешно записана в историю")
                } else {
                    Log.e("WorkoutDetailActivity", "Ошибка при записи тренировки в историю: ${result.exceptionOrNull()?.message}")
                }

                val weightResult = recordWeightStatistic(userId, weight, timestamp)
                if (weightResult.isSuccess) {
                    Log.d("WorkoutDetailActivity", "Вес пользователя успешно записан")
                } else {
                    Log.e("WorkoutDetailActivity", "Ошибка при записи веса пользователя: ${weightResult.exceptionOrNull()?.message}")
                }
            }
        }

        val intent = Intent(this, StatisticsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
