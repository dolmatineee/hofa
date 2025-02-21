package com.example.hofa.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.hofa.data.dao.StatisticsDao
import com.example.hofa.data.entities.Goal
import com.example.hofa.data.entities.Session
import com.example.hofa.data.entities.Statistics
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class StatisticsRepository @Inject constructor(
    private val statisticsDao: StatisticsDao,
) {
    private val gson = Gson()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateStatistics(session: Session) {
        val currentDate = LocalDate.now().toString()
        val currentHour = LocalTime.now().hour

        // Получаем последнюю цель
        val latestGoal = statisticsDao.getLatestGoal()
        val targetSessions = latestGoal?.targetSessions ?: 0 // Если цели нет, используем 0

        val existingStatistics = statisticsDao.getStatisticsByDate(currentDate)
        if (existingStatistics != null) {
            // Обновляем существующую статистику
            val sessionsByHourMap = existingStatistics.toMap().toMutableMap()
            sessionsByHourMap[currentHour] = sessionsByHourMap.getOrDefault(currentHour, 0) + 1

            val updatedStatistics = existingStatistics.copy(
                sessionsByHour = Statistics.fromMap(sessionsByHourMap),
                targetSessions = targetSessions, // Обновляем targetSessions
                totalSessions = existingStatistics.totalSessions + 1,
                totalRounds = existingStatistics.totalRounds + session.rounds,
                totalDuration = existingStatistics.totalDuration + session.duration,
                totalBreaths = existingStatistics.totalBreaths + session.breaths,
                totalBreathHoldDuration = existingStatistics.totalBreathHoldDuration + session.breathHoldDuration
            )
            statisticsDao.updateStatistics(updatedStatistics)
        } else {
            // Создаем новую запись статистики
            val newStatistics = Statistics(
                date = currentDate,
                sessionsByHour = Statistics.fromMap(mapOf(currentHour to 1)),
                targetSessions = targetSessions, // Используем targetSessions из goals
                totalSessions = 1,
                totalRounds = session.rounds,
                totalDuration = session.duration,
                totalBreaths = session.breaths,
                totalBreathHoldDuration = session.breathHoldDuration
            )
            statisticsDao.insertStatistics(newStatistics)
        }
    }

    fun getStatisticsForDate(date: String): Flow<Statistics?> {
        return statisticsDao.getStatisticsByDateLiveData(date)
    }

    fun getAllStatistics(): LiveData<List<Statistics>> {
        return statisticsDao.getAllStatistics()
    }

    suspend fun getGoal(): Int {
        return statisticsDao.getLatestGoal().targetSessions
    }

    suspend fun updateGoal(targetSession: Int) {
        statisticsDao.updateGoal(Goal(targetSessions = targetSession))
    }
}