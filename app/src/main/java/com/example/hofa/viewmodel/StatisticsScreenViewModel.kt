package com.example.hofa.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hofa.data.entities.ChartData
import com.example.hofa.data.entities.Statistics
import com.example.hofa.data.repository.StatisticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class StatisticsScreenViewModel @Inject constructor(
    private val statisticsRepository: StatisticsRepository
) : ViewModel() {

    // Текущая дата
    @RequiresApi(Build.VERSION_CODES.O)
    private val _currentDate = MutableStateFlow(LocalDate.now())
    val currentDate: StateFlow<LocalDate> @RequiresApi(Build.VERSION_CODES.O) get() = _currentDate

    // Статистика для текущей даты
    private val _statistics = MutableStateFlow<Statistics?>(null)
    val statistics: StateFlow<Statistics?> get() = _statistics

    private val _targetSessions = MutableStateFlow<Int?>(null)
    val targetSessions: StateFlow<Int?> get() = _targetSessions

    private val _showGoalPicker = MutableStateFlow(false)
    val showGoalPicker: StateFlow<Boolean> get() = _showGoalPicker

    fun updateGoal(newGoal: Int) {
        viewModelScope.launch {
            statisticsRepository.updateGoal(newGoal)
            _targetSessions.value = newGoal
        }
    }

    fun showGoalPicker() {
        _showGoalPicker.value = true
    }

    fun hideGoalPicker() {
        _showGoalPicker.value = false
    }

    private fun getGoal(){
        viewModelScope.launch {
            val goal =  statisticsRepository.getGoal()
            _targetSessions.value = goal
        }
    }

    init {
        loadStatistics(LocalDate.now())
        getGoal()
    }
    // Загрузка статистики
    fun loadStatistics(date: LocalDate) {
        viewModelScope.launch {
            val stats = statisticsRepository.getStatisticsForDate(date.toString())
            _statistics.value = stats.first()
        }
    }

    // Переключение на предыдущую дату
    @RequiresApi(Build.VERSION_CODES.O)
    fun previousDate() {
        _currentDate.value = _currentDate.value.minusDays(1)
        loadStatistics(_currentDate.value)
    }

    // Переключение на следующую дату
    @RequiresApi(Build.VERSION_CODES.O)
    fun nextDate() {
        _currentDate.value = _currentDate.value.plusDays(1)
        loadStatistics(_currentDate.value)
    }

    // Вычисление адаптивных значений для диаграммы
    fun getChartData(statistics: Statistics?): ChartData {
        val stats = statistics
        val sessionsByHour = stats?.toMap() ?: mapOf() // Преобразуем JSON-строку в Map<Int, Int>

        // Максимальное количество сеансов для масштабирования
        val maxSessions = sessionsByHour.values.maxOrNull() ?: 0

        // Адаптивные значения для разметки (0, 2, 4, 6 или 0, 10, 20, 30 и т.д.)
        val yAxisLabels = when {
            maxSessions <= 6 -> listOf(0, 2, 4, 6)
            maxSessions <= 15 -> listOf(0, 5, 10 ,15)
            maxSessions <= 30 -> listOf(0, 10, 20, 30)
            else -> listOf(0, 100, 200, 300)
        }

        // Создаем список всех 24 часов
        val xAxisLabels = (0..24).toList()

        return ChartData(
            sessionsByHour = sessionsByHour,
            yAxisLabels = yAxisLabels,
            xAxisLabels = xAxisLabels // Все 24 часа
        )
    }
}