package com.example.darfit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardingActivity : AppCompatActivity() {
    companion object {
        var personalData = PersonalData(
            id = 0,
            fullName = "",
            contactInfo = "",
            password = "",
            birthDate = "",
            gender = "",
            weight = 0.0,
            height = 0.0,
            fitnessLevel = null,
            goal = null
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val isRegistered = sharedPreferences.getBoolean("isRegistered", false)

        if (isRegistered) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(R.layout.activity_on_boarding)

            val viewPager: ViewPager2 = findViewById(R.id.viewPagers)
            viewPager.adapter = OnboardingAdapter(this)

            val tabLayout: TabLayout = findViewById(R.id.tabLayout)
            TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewPager.isUserInputEnabled = position < 3
                }
            })

            loadFragment(RegistrFragment1())
        }
    }


    private fun isFirstLaunch(): Boolean {
        val sharedPreferences = getSharedPreferences("appPreferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isFirstLaunch", true)
    }

    private fun markFirstLaunchComplete() {
        val editor = getSharedPreferences("appPreferences", MODE_PRIVATE).edit()
        editor.putBoolean("isFirstLaunch", false)
        editor.apply()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }
}
