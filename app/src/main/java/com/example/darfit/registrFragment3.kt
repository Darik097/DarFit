package com.example.darfit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.commit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class registrFragment3 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var buttonNext2: Button
    private lateinit var editTextWeight: EditText
    private lateinit var editTextGoal: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registr3, container, false)

        editTextWeight = view.findViewById(R.id.editTextWeight)
        editTextGoal = view.findViewById(R.id.editTextGoal)
        buttonNext2 = view.findViewById(R.id.buttonNext2)

        buttonNext2.setOnClickListener {
            saveWeightData()
        }

        return view
    }

    private fun saveWeightData() {
        val weightText = editTextWeight.text.toString()
        val goalText = editTextGoal.text.toString()

        if (weightText.isNotEmpty() && goalText.isNotEmpty()) {
            val weight = weightText.toDoubleOrNull()
            val goal = goalText.toDoubleOrNull()

            if (weight != null && goal != null) {
                OnBoardingActivity.personalData.weight = weight
                OnBoardingActivity.personalData.goal = goal
                navigateToNextFragment2()
            } else {
                showError("Пожалуйста, введите корректные значения для веса и цели")
            }
        } else {
            showError("Пожалуйста, введите свой вес и цель")
        }
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToNextFragment2() {
        parentFragmentManager.commit {
            replace(R.id.fragmentContainer, RegistrFragment6()) // Переход к следующему фрагменту
            addToBackStack(null) // Добавляем в стек, чтобы была возможность вернуться назад
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            registrFragment3().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
