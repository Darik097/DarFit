package com.example.darfit

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.darfit.api.ApiMethods.getStatisticsData
import com.example.darfit.api.ApiMethods.getWorkoutHistory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StatisticsActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        lineChart = findViewById(R.id.lineChart)
        setupLineChart()


        // Получаем userId из SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)


        if (userId != -1 && userId != 0) {
            loadChartData(userId)
            loadWorkoutHistory(userId)
        } else {
            Toast.makeText(this, "Ошибка: ID пользователя не найден", Toast.LENGTH_SHORT).show()
            Log.e("StatisticsActivity", "User ID is not found or invalid")
        }
    }

    private fun setupLineChart() {
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.isEnabled = false

        lineChart.xAxis.textColor = Color.WHITE
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)

        lineChart.description.isEnabled = false

        val legend: Legend = lineChart.legend
        legend.isEnabled = true
        legend.textColor = Color.WHITE
        legend.textSize = 12f
    }

    private fun loadChartData(userId: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val result = getStatisticsData(userId)
                if (result.isSuccess) {
                    val weightRecords = result.getOrNull() ?: emptyList()
                    setupChartWithData(weightRecords)
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                    Toast.makeText(this@StatisticsActivity, "Ошибка загрузки данных: $errorMessage", Toast.LENGTH_SHORT).show()
                    Log.e("StatisticsActivity", "Error loading data: $errorMessage")
                }
            } catch (e: Exception) {
                Toast.makeText(this@StatisticsActivity, "Ошибка при запросе: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("StatisticsActivity", "Error: ${e.message}", e)
            }
        }
    }


    private fun setupChartWithData(weightRecords: List<Statistic>) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Исходный формат даты
        val shortDateFormat = SimpleDateFormat("dd/MM", Locale.getDefault()) // Краткий формат даты

        val dates = mutableListOf<String>() // Массив для оси X (даты)
        val weights = mutableListOf<Float>() // Массив для оси Y (веса)

        for (record in weightRecords) {
            val date = record.timestamp
            val weight = record.weight

            try {
                // Преобразуем строку даты в краткий формат
                val parsedDate = dateFormat.parse(date)
                val shortDate = shortDateFormat.format(parsedDate)

                dates.add(shortDate)
                weights.add(weight.toFloat())

                // Логируем каждую дату и вес
                Log.d("StatisticsActivity", "Дата: $shortDate, Вес: $weight")
            } catch (e: Exception) {
                Log.e("StatisticsActivity", "Ошибка парсинга даты: ${e.message}")
            }
        }

        // Логируем все данные
        Log.d("StatisticsActivity", "Даты для графика: $dates")
        Log.d("StatisticsActivity", "Веса для графика: $weights")

        // Создаём entries для графика
        val dataEntries = weights.mapIndexed { index, weight ->
            Entry(index.toFloat(), weight)
        }

        // Настраиваем ось X
        lineChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(dates)
            textSize = 12f
            labelRotationAngle = -45f
            granularity = 1f
            isGranularityEnabled = true
            setAvoidFirstLastClipping(true)
        }

        // Настраиваем график
        val lineDataSet = LineDataSet(dataEntries, "Вес по датам")
        lineDataSet.color = Color.parseColor("#B726FF")
        lineDataSet.valueTextColor = Color.WHITE
        lineDataSet.valueTextSize = 16f
        lineDataSet.setDrawCircles(true)
        lineDataSet.setCircleColor(Color.parseColor("#B726FF"))
        lineDataSet.lineWidth = 2f

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        lineChart.legend.textSize = 16f
        lineChart.invalidate()
    }



    private fun loadWorkoutHistory(userId: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val result = getWorkoutHistory(userId)
                if (result.isSuccess) {
                    val workoutHistoryList = result.getOrNull() ?: emptyList()
                    setupRecyclerView(workoutHistoryList)
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                    Toast.makeText(this@StatisticsActivity, "Ошибка загрузки данных: $errorMessage", Toast.LENGTH_SHORT).show()
                    Log.e("StatisticsActivity", "Error loading data: $errorMessage")
                }
            } catch (e: Exception) {
                Toast.makeText(this@StatisticsActivity, "Ошибка при запросе: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("StatisticsActivity", "Error: ${e.message}", e)
            }
        }
    }

    private fun setupRecyclerView(workoutHistoryList: List<WorkoutHistoryView>) {
        val recyclerView = findViewById<RecyclerView>(R.id.workoutHistoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = WorkoutHistoryAdapter(workoutHistoryList)
        recyclerView.adapter = adapter
    }
}

