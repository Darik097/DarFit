package com.example.darfit

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.darfit.api.ApiMethods.authPersonalData
import com.example.darfit.api.ApiMethods.updatePersonalData
import com.example.darfit.databinding.ActivityPrivateDataBinding
import kotlinx.coroutines.launch

class PrivateData : AppCompatActivity() {
    private lateinit var binding: ActivityPrivateDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val login = sharedPreferences.getString("user_email", null)
        val password = sharedPreferences.getString("password", null)
        val userId = sharedPreferences.getInt("user_id", -1)

        if (login != null && password != null && userId != -1 && userId != 0) {
            lifecycleScope.launch {
                val result = authPersonalData(login, password)
                result.onSuccess { personalData ->
                    binding.editTextFullName.setText(personalData.fullName)
                    binding.editTextContactInfo.setText(personalData.contactInfo)
                    binding.editTextBirthDate.setText(personalData.birthDate)
                    binding.editTextGender.setText(personalData.gender)
                    binding.editTextWeight.setText(personalData.weight.toString())
                    binding.editTextHeight.setText(personalData.height.toString())
                    binding.editTextFitnessLevel.setText(personalData.fitnessLevel ?: "")
                    binding.editTextGoal.setText(personalData.goal?.toString() ?: "")
                }.onFailure { exception ->
                    Toast.makeText(this@PrivateData, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Нет логина, пароля или ID", Toast.LENGTH_SHORT).show()
        }

        binding.buttonSave.setOnClickListener {
            if (userId != -1) {
                val updatedPersonalData = PersonalData(
                    id = userId,
                    fullName = binding.editTextFullName.text.toString(),
                    contactInfo = binding.editTextContactInfo.text.toString(),
                    password = password ?: "",
                    birthDate = binding.editTextBirthDate.text.toString(),
                    gender = binding.editTextGender.text.toString(),
                    weight = binding.editTextWeight.text.toString().toDoubleOrNull() ?: 0.0,
                    height = binding.editTextHeight.text.toString().toDoubleOrNull() ?: 0.0,
                    fitnessLevel = binding.editTextFitnessLevel.text.toString(),
                    goal = binding.editTextGoal.text.toString().toDoubleOrNull()
                )

                lifecycleScope.launch {
                    val updateResult = updatePersonalData(userId, updatedPersonalData)
                    updateResult.onSuccess {
                        val editor = sharedPreferences.edit()
                        editor.putString("user_name", updatedPersonalData.fullName)
                        editor.putString("user_email", updatedPersonalData.contactInfo)
                        editor.putString("user_birthDate", updatedPersonalData.birthDate)
                        editor.putString("user_gender", updatedPersonalData.gender)
                        editor.putFloat("user_height", updatedPersonalData.height.toFloat())
                        editor.putFloat("user_weight", updatedPersonalData.weight.toFloat())
                        editor.putFloat("user_goal", updatedPersonalData.goal?.toFloat() ?: 0f)
                        editor.putString("user_fitness", updatedPersonalData.fitnessLevel)
                        editor.apply()

                        Toast.makeText(this@PrivateData, "Данные успешно обновлены", Toast.LENGTH_SHORT).show()
                    }.onFailure { exception ->
                        Toast.makeText(this@PrivateData, "Ошибка: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Невозможно сохранить: отсутствует ID пользователя", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonOut.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            val intent = Intent(this, OnBoardingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
