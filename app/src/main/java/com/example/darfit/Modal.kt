package com.example.darfit

import com.google.gson.annotations.SerializedName

data class PersonalData(
    @SerializedName("id") var id: Int,
    @SerializedName("full_name") var fullName: String,
    @SerializedName("contact_info") var contactInfo: String,
    @SerializedName("password") var password: String,
    @SerializedName("birth_date") var birthDate: String,
    @SerializedName("gender") var gender: String,
    @SerializedName("weight") var weight: Double,
    @SerializedName("height") var height: Double,
    @SerializedName("fitness_level") var fitnessLevel: String?,
    @SerializedName("goal") var goal: Double?
)

data class Workout(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String
)

data class Exercise(
    @SerializedName("id") val id: Int,
    @SerializedName("workout_id") val workoutId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("workout_text") val workoutText: String?,
    @SerializedName("workout_time_minutes") val workoutTimeMinutes: Int,
    @SerializedName("calories_burned") val caloriesBurned: Int,
    @SerializedName("image_url") val imageUrl: String?
)

data class Statistic(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("weight") val weight: Double,
    @SerializedName("timestamp") val timestamp: String
)

data class WorkoutHistory(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("workout_id") val workoutId: Int,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("workout_length") val workoutLength: Int
)


data class WorkoutHistoryView(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("workout_id") val workoutId: Int,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("workout_length") val workoutLength: Int,
    @SerializedName("calories") val calories: Int,
    @SerializedName("image_url") val imageUrl: String
)


data class Nutrition(
    @SerializedName("id") val id: Int,
    @SerializedName("recipe_name") val recipeName: String,
    @SerializedName("calories") val calories: Int,
    @SerializedName("protein") val protein: Double,
    @SerializedName("fat") val fat: Double,
    @SerializedName("carbohydrates") val carbohydrates: Double,
    @SerializedName("recipe_text") val recipeText: String,
    @SerializedName("image_url") val imageUrl: String
)
