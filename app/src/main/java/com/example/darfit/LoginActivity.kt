package com.example.darfit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.darfit.api.ApiMethods
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var buttonAuth: Button
    private lateinit var textInputLayoutEmail: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)
        if (userId != -1 && userId != 0) {
            val intent = Intent(this@LoginActivity, ActivityForFragment::class.java)
            startActivity(intent)
            finish()
            return
        }

        buttonAuth = findViewById(R.id.buttonAuth)
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail)
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword)
        val haveAccount: TextView = findViewById(R.id.haveAccount)

        haveAccount.setOnClickListener {
            val intent = Intent(this@LoginActivity, OnBoardingActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonAuth.setOnClickListener {
            val contactInfo = textInputLayoutEmail.editText?.text.toString()
            val password = textInputLayoutPassword.editText?.text.toString()

            CoroutineScope(Dispatchers.Main).launch {
                ApiMethods.authPersonalData(contactInfo, password).onSuccess { user ->
                    val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putInt("user_id", user.id)
                        putString("user_email", user.contactInfo)
                        putString("user_name", user.fullName)
                        putString("user_birthDate", user.birthDate)
                        putString("user_gender", user.gender)
                        putFloat("user_height", user.height.toFloat())
                        putFloat("user_weight", user.weight.toFloat())
                        putFloat("user_goal", user.goal?.toFloat() ?: 0f)
                        putString("user_fitness", user.fitnessLevel)
                        putString("password", user.password)
                        apply()
                    }

                    val intent = Intent(this@LoginActivity, ActivityForFragment::class.java)
                    startActivity(intent)
                    finish()
                }.onFailure {
                    Toast.makeText(this@LoginActivity, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
                }
            }
            Log.d("Dinar", "User info: ${OnBoardingActivity.personalData}")
        }
    }
}
