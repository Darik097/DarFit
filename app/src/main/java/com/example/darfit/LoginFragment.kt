package com.example.darfit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var buttonAuth: Button
    private lateinit var textInputLayoutEmail: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout

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
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Find the TextView5 in the inflated view
        val textView5: TextView = view.findViewById(R.id.textView5)
        val buttonAuth: Button = view.findViewById(R.id.buttonAuth)
        val textInputLayoutEmail = view.findViewById<TextInputLayout>(R.id.textInputLayoutEmail)
        val textInputLayoutPassword = view.findViewById<TextInputLayout>(R.id.textInputLayoutPassword)

        buttonAuth.setOnClickListener {
            val contactInfo = textInputLayoutEmail.editText?.text.toString()
            val password = textInputLayoutPassword.editText?.text.toString()

            CoroutineScope(Dispatchers.Main).launch {
                ApiMethods.authPersonalData(contactInfo, password).onSuccess {
                    OnBoardingActivity.personalData.id = it.id
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }.onFailure {
                    Toast.makeText(this@LoginFragment.requireContext(), "Ошибка авторизации", Toast.LENGTH_SHORT).show()
                }
            }
            Log.d("Dinar", OnBoardingActivity.personalData.toString())
        }

        textView5.setOnClickListener {
            val intent = Intent(activity, ActivityForRegistr::class.java)

            intent.putExtra("extraData", "someValue")
            startActivity(intent)
            activity?.finish()

            // Показать сообщение (Toast)
            Toast.makeText(requireContext(), "Вы перешли на RegistrActivity!", Toast.LENGTH_SHORT).show()
        }



        return view
    }

    private fun loadFragment(fragment: Fragment) {
        val activity = requireActivity()

        if (fragment is RegistrFragment1) {
            val intent = Intent(activity, OnBoardingActivity::class.java)
            intent.putExtra("startFragment", "RegistrFragment1")
            activity.startActivity(intent)
            activity.finish()
        } else {
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
