package com.example.darfit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class AuthActivity : AppCompatActivity() {

    private lateinit var textView5: TextView
    private lateinit var textView6: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        textView5 = findViewById(R.id.textView5)
        textView6 = findViewById(R.id.textView6)

        loadFragment(LoginFragment())

        textView6.setOnClickListener {
            loadFragment(LoginFragment())
        }

        textView5.setOnClickListener {
            loadFragment(RegistrFragment1())
            finish()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }
}
