package com.example.darfit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.darfit.api.ApiMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NutritionFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var nutritionAdapter: NutritionAdapter
    private var nutritionList: List<Nutrition> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_nutrition, container, false)

        recyclerView = view.findViewById(R.id.nutritionRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchNutritionData()

        return view
    }

    private fun fetchNutritionData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    ApiMethods.getNutritionData()
                }

                if (isAdded) {
                    if (result.isSuccess) {
                        nutritionList = result.getOrNull() ?: mutableListOf()
                        nutritionAdapter = NutritionAdapter(nutritionList)
                        recyclerView.adapter = nutritionAdapter
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Ошибка при загрузке данных",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                if (isAdded) {
                    Toast.makeText(
                        requireContext(),
                        "Ошибка: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
