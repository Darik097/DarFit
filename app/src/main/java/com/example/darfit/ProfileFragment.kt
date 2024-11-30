package com.example.darfit

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.darfit.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setupClickListeners()

        return binding.root
    }

    private fun setupClickListeners() {
        binding.personalDataTextView.setOnClickListener {
            val intent = Intent(requireContext(), PrivateData::class.java)
            startActivity(intent)
        }

        binding.statisticsTextView.setOnClickListener {
            val intent = Intent(requireContext(), StatisticsActivity::class.java)
            startActivity(intent)
        }

        binding.askQuestionTextView.setOnClickListener {
            val telegramUsername = "Darik097"
            val telegramUri = "https://t.me/$telegramUsername"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(telegramUri))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
