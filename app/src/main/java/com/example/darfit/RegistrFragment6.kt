package com.example.darfit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrFragment6.newInstance] factory method to
 * create an instance of this fragment.
 */

class RegistrFragment6 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var heightEditText: EditText
    private lateinit var buttonNext3: Button

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
        val view = inflater.inflate(R.layout.fragment_registr6, container, false)

        heightEditText = view.findViewById(R.id.editTextHeight)
        buttonNext3 = view.findViewById(R.id.buttonNext3)

        buttonNext3.setOnClickListener {
            val height = heightEditText.text.toString().trim()

            if (height.isNotEmpty()) {
                val bundle = Bundle().apply {
                    putString("USER_HEIGHT", height)
                }
                val nextFragment = RegistrFragment4()
                nextFragment.arguments = bundle
                
                OnBoardingActivity.personalData.height = height.toDouble()


                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, nextFragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "Пожалуйста, введите ваш рост", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrFragment6().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
