package com.example.darfit.api

import android.content.Context
import android.util.Log
import com.example.darfit.Exercise
import com.example.darfit.Nutrition
import com.example.darfit.OnBoardingActivity
import com.example.darfit.PersonalData
import com.example.darfit.Statistic
import com.example.darfit.Workout
import com.example.darfit.WorkoutHistory
import com.example.darfit.WorkoutHistoryView
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ApiMethods {

    suspend fun addPersonalData(personalData: PersonalData): Result<Boolean> = coroutineScope {
        val usersApi = RetrofitInstance.getRetrofitInstanceDefault().create(PersonApi::class.java)
        return@coroutineScope try {
            val response = usersApi.createPersonalData(personalData)

            if (!response.isSuccessful || response.body() == null) {
                throw Exception(response.body().toString())
            } else {
                Result.success(true)
            }
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun authPersonalData(login: String, password: String): Result<PersonalData> = coroutineScope {
        val usersApi = RetrofitInstance.getRetrofitInstanceDefault().create(PersonApi::class.java)
        return@coroutineScope try {
            val response = usersApi.getPersonalData(login, password)

            if (!response.isSuccessful || response.body() == null) {
                throw Exception(response.body().toString())
            } else {
                OnBoardingActivity.personalData = response.body()!!
                Result.success(response.body()!!)
            }
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getNutritionData(): Result<List<Nutrition>> = coroutineScope {
        val nutritionApi = RetrofitInstance.getRetrofitInstanceDefault().create(PersonApi::class.java)
        return@coroutineScope try {
            val response = nutritionApi.getAllNutrition()

            if (!response.isSuccessful || response.body() == null) {
                throw Exception("Error: ${response.errorBody()?.string()}")
            } else {
                Result.success(response.body()!!)
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Failed to fetch nutrition data: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getWorkouts(): Result<List<Workout>> = coroutineScope {
        val workoutApi = RetrofitInstance.getRetrofitInstanceDefault().create(PersonApi::class.java)
        return@coroutineScope try {
            val response = workoutApi.getWorkouts()

            if (!response.isSuccessful || response.body() == null) {
                throw Exception("Error: ${response.errorBody()?.string()}")
            } else {
                Result.success(response.body()!!)
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Failed to fetch workouts: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getExerciseForWorkout(workoutId: Int): Result<List<Exercise>> = coroutineScope {
        val workoutApi = RetrofitInstance.getRetrofitInstanceDefault().create(PersonApi::class.java)

        return@coroutineScope try {
            val response = workoutApi.getExercises(workoutId)

            if (!response.isSuccessful || response.body() == null) {
                throw Exception("Error: ${response.errorBody()?.string()}")
            } else {
                // Получаем список упражнений
                val exercises = response.body()!!
                Result.success(exercises)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun updatePersonalData(id: Int, personalData: PersonalData): Result<Unit> = coroutineScope {
        val usersApi = RetrofitInstance.getRetrofitInstanceDefault().create(PersonApi::class.java)
        return@coroutineScope try {
            val response = usersApi.updatePersonalData(id, personalData)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun recordWorkoutHistory(userId: Int, workoutId: Int, timestamp: Long, workoutLength: Int): Result<Unit> = coroutineScope {
        val api = RetrofitInstance.getRetrofitInstanceDefault().create(PersonApi::class.java)
        return@coroutineScope try {
            val timestampString = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(
                Date(timestamp)
            )
            val workoutHistory = WorkoutHistory(userId, workoutId, timestampString, workoutLength )

            Log.d("WorkoutDetailActivity", "Sending workout data: $workoutHistory")

            val response = api.addWorkoutHistory(workoutHistory)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("WorkoutDetailActivity", "Error while recording workout history: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun recordWeightStatistic(userId: Int, weight: Double, timestamp: Long): Result<Unit> = coroutineScope {
        val api = RetrofitInstance.getRetrofitInstanceDefault().create(PersonApi::class.java)
        return@coroutineScope try {

            val timestampString = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(
                Date(timestamp)
            )

            val statistic = Statistic(userId, weight, timestampString)

            val response = api.createStatistic(statistic)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStatisticsData(userId: Int): Result<List<Statistic>> = coroutineScope {
        val api = RetrofitInstance.getRetrofitInstanceDefault().create(PersonApi::class.java)
        return@coroutineScope try {
            val response = api.getStatistics(userId)
            if (response.isSuccessful) {
                val statistics = response.body() ?: emptyList()
                Result.success(statistics)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWorkoutHistory(userId: Int): Result<List<WorkoutHistoryView>> = coroutineScope {
        val api = RetrofitInstance.getRetrofitInstanceDefault().create(PersonApi::class.java)
        return@coroutineScope try {
            val response = api.getWorkoutHistory(userId)
            if (response.isSuccessful) {
                val workoutHistory = response.body() ?: emptyList()
                Result.success(workoutHistory)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}