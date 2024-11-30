package com.example.darfit.api

import com.example.darfit.Exercise
import com.example.darfit.Nutrition
import com.example.darfit.PersonalData
import com.example.darfit.Statistic
import com.example.darfit.Workout
import com.example.darfit.WorkoutHistory
import com.example.darfit.WorkoutHistoryView
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface PersonApi {
    // Получить персональные данные по контактной информации и паролю
    @GET("/person/personal_data/auth")
    suspend fun getPersonalData(
        @Query("contact_info") contactInfo: String,
        @Query("password") password: String
    ): Response<PersonalData>

    // Создать новые персональные данные
    @POST("/person/personal_data/")
    suspend fun createPersonalData(
        @Body personalData: PersonalData
    ): Response<Map<String, String>>

    // Получить все персональные данные
    @GET("/person/personal_data/")
    suspend fun getAllPersonalData(): Response<List<PersonalData>>

    // Получить персональные данные по ID
    @GET("/person/personal_data/{id}")
    suspend fun getPersonalDataById(
        @Path("id") id: Int
    ): Response<PersonalData>

    // Обновить персональные данные
    @PUT("/person/personal_data/{id}")
    suspend fun updatePersonalData(
        @Path("id") id: Int,
        @Body personalData: PersonalData
    ): Response<Map<String, String>>

    // Удалить персональные данные по ID
    @DELETE("/person/personal_data/{id}")
    suspend fun deletePersonalData(
        @Path("id") id: Int
    ): Response<Map<String, String>>

    // Получить все данные о питании
    @GET("/person/nutrition/")
    suspend fun getAllNutrition(): Response<List<Nutrition>>

    // Получить все типы тренировок
    @GET("workout/workouts/")
    suspend fun getWorkouts(): Response<List<Workout>>

    @GET("workout/workouts/{id}/exercises")
    suspend fun getExercises(@Path("id") workoutId: Int): Response<List<Exercise>>

    @POST("statistics/workout_history/")
    suspend fun addWorkoutHistory(
        @Body workoutHistory: WorkoutHistory
    ): Response<Unit>

    @POST("statistics/statistics/")
    suspend fun createStatistic(@Body statistic: Statistic): Response<Unit>


    @GET("statistics/statistics/{id}")
    suspend fun getStatistics(@Path("id") userId: Int): Response<List<Statistic>>

    @GET("statistics/workout_history/{userId}")
    suspend fun getWorkoutHistory(@Path("userId") userId: Int): Response<List<WorkoutHistoryView>>


}
