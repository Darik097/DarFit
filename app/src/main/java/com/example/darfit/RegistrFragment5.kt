package com.example.darfit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.darfit.api.ApiMethods
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrFragment5.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrFragment5 : Fragment() {
    private lateinit var textInputLayoutFullname: TextInputLayout
    private lateinit var textInputLayoutUsername: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout
    private lateinit var haveAccount: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registr5, container, false)

        val textInputLayoutFullname = view.findViewById<TextInputLayout>(R.id.textInputLayoutFullname)
        val textInputLayoutUsername = view.findViewById<TextInputLayout>(R.id.textInputLayoutUsername)
        val textInputLayoutPassword = view.findViewById<TextInputLayout>(R.id.textInputLayoutPassword)
        val registerButton: Button = view.findViewById(R.id.buttonRegister)

        val haveAccount: TextView = view.findViewById(R.id.haveAccount)

        haveAccount.setOnClickListener {
            // Переход к активности входа
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish()  // Закрываем текущую активность
        }

        registerButton.setOnClickListener {
            val fullName = textInputLayoutFullname.editText?.text.toString()
            val contactInfo = textInputLayoutUsername.editText?.text.toString()
            val password = textInputLayoutPassword.editText?.text.toString()

            OnBoardingActivity.personalData.fullName = fullName
            OnBoardingActivity.personalData.contactInfo = contactInfo
            OnBoardingActivity.personalData.password = password

            CoroutineScope(Dispatchers.Main).launch {
                ApiMethods.addPersonalData(OnBoardingActivity.personalData).onSuccess {

                    Toast.makeText(requireContext(), "Успешная регистрация", Toast.LENGTH_SHORT).show()

                    // Сохраняем информацию о регистрации в SharedPreferences
                    val sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putInt("user_id", OnBoardingActivity.personalData.id)
                        putString("user_email", OnBoardingActivity.personalData.contactInfo)
                        putString("user_name", OnBoardingActivity.personalData.fullName)
                        putString("user_birthDate", OnBoardingActivity.personalData.birthDate)
                        putString("user_gender", OnBoardingActivity.personalData.gender)
                        putFloat("user_height", OnBoardingActivity.personalData.height.toFloat())
                        putFloat("user_weight", OnBoardingActivity.personalData.weight.toFloat())
                        putFloat("user_goal", OnBoardingActivity.personalData.goal?.toFloat() ?: 0f)
                        putString("user_fitness", OnBoardingActivity.personalData.fitnessLevel)
                        putString("password", OnBoardingActivity.personalData.password)
                        apply()
                    }

                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }.onFailure {
                    Toast.makeText(requireContext(), "Ошибка регистрации", Toast.LENGTH_SHORT).show()
                    Log.e("Errors", "", it)
                }
            }
            Log.d("Dinar", OnBoardingActivity.personalData.toString())
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrFragment5().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
