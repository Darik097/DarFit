package com.example.darfit

import CenterZoomLayoutManager
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [RegistrFragment4.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrFragment4 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var levelsRecyclerView: RecyclerView
    private lateinit var levelsAdapter: LevelsAdapter
    private var selectedLevel: String? = "Средний"

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
        val view = inflater.inflate(R.layout.fragment_registr4, container, false)

        val levels = listOf("Новичок", "Любитель", "Средний", "Продвинутый", "Эксперт")

        levelsRecyclerView = view.findViewById(R.id.levelsRecyclerView)
        levelsAdapter = LevelsAdapter(levels) { level ->
            selectedLevel = level
        }

        // Настройка кастомного LayoutManager
        val layoutManager = CenterZoomLayoutManager(requireContext())
        levelsRecyclerView.layoutManager = layoutManager
        levelsRecyclerView.adapter = levelsAdapter

        // SnapHelper для фиксации элемента по центру
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(levelsRecyclerView)

        levelsRecyclerView.post {
            levelsRecyclerView.scrollToPosition(2)
            updateSelectedLevel(layoutManager)
        }

        levelsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateSelectedLevel(layoutManager)
                }
            }
        })

        val buttonNext3: Button = view.findViewById(R.id.buttonNext3)
        buttonNext3.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainer, RegistrFragment5()) // Переход к следующему фрагменту
                addToBackStack(null) // Добавляем в стек, чтобы была возможность вернуться назад
            }
        }

        return view
    }

    private fun updateSelectedLevel(layoutManager: LinearLayoutManager) {
        val centerView = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition() + 1)
        centerView?.let {
            val centerPosition = layoutManager.getPosition(centerView)
            selectedLevel = levelsAdapter.getItem(centerPosition)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrFragment4().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

