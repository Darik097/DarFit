package com.example.darfit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.commit
import java.text.SimpleDateFormat
import java.util.*

// Параметры для аргументов фрагмента
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegistrFragment2 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var buttonNext: Button
    private lateinit var editTextBirthDate: EditText

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
        val view = inflater.inflate(R.layout.fragment_registr2, container, false)

        editTextBirthDate = view.findViewById(R.id.editTextAge)
        buttonNext = view.findViewById(R.id.buttonNext1)

        buttonNext.setOnClickListener {
            if (saveBirthDate()) {
                navigateToNextFragment()
            }
        }

        return view
    }

    private fun saveBirthDate(): Boolean {
        val dateText = editTextBirthDate.text.toString()
        if (dateText.isEmpty()) {
            editTextBirthDate.error = "Дата рождения не может быть пустой"
            return false
        }

        try {
            // Используем правильный формат: год-месяц-день
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val birthDate = dateFormat.parse(dateText)

            // Сохраняем дату в нужном формате, если нужно, можете также преобразовать в строку
            OnBoardingActivity.personalData.birthDate = dateFormat.format(birthDate)
        } catch (e: Exception) {
            editTextBirthDate.error = "Введите дату в формате гггг-MM-дд"
            return false
        }

        return true
    }



    private fun navigateToNextFragment() {
        parentFragmentManager.commit {
            replace(R.id.fragmentContainer, registrFragment3())
            addToBackStack(null)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
