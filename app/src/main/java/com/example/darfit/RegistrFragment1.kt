package com.example.darfit

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.commit
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.button.MaterialButton

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegistrFragment1 : Fragment() {
    private lateinit var buttonMale: MaterialButton
    private lateinit var buttonFemale: MaterialButton
    private lateinit var buttonNext: Button
    private lateinit var goLogin: ImageButton
    private var isGenderSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registr1, container, false)

        buttonMale = view.findViewById(R.id.buttonMale)
        buttonFemale = view.findViewById(R.id.buttonFemale)
        buttonNext = view.findViewById(R.id.buttonNext)


        val goLogin: ImageView = view.findViewById(R.id.goLogin)

        goLogin.setOnClickListener {
            val intent = Intent(requireActivity(), OnBoardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

            requireActivity().finish()
        }


        buttonMale.setOnClickListener {
            changeButtonBackground(buttonMale, buttonFemale)
            OnBoardingActivity.personalData.gender = "Мужской"
            isGenderSelected = true
        }

        buttonFemale.setOnClickListener {
            changeButtonBackground(buttonFemale, buttonMale)
            OnBoardingActivity.personalData.gender = "Женский"
            isGenderSelected = true
        }

        buttonNext.setOnClickListener {
            if (isGenderSelected) {
                navigateToNextFragment()
            } else {
                showGenderSelectionError()
            }
        }

        return view
    }

    private fun navigateToNextFragment() {
        parentFragmentManager.commit {
            replace(R.id.fragmentContainer, RegistrFragment2())
            addToBackStack(null)
        }
    }

    private fun changeButtonBackground(selectedButton: MaterialButton, otherButton: MaterialButton) {
        selectedButton.setBackgroundColor(Color.parseColor("#B726FF"))
        otherButton.setBackgroundColor(Color.LTGRAY)
    }

    private fun showGenderSelectionError() {
        Toast.makeText(context, "Пожалуйста, выберите пол", Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrFragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


