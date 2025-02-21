package com.example.hofa.data.repository

import com.example.hofa.data.dao.StreakDao
import com.example.hofa.data.entities.Streak
import java.util.Calendar

class StreakRepository(private val streakDao: StreakDao) {

    suspend fun getStreak(): Streak {
        return streakDao.getStreak()
    }

    suspend fun updateStreak() {
        val currentStreak = streakDao.getStreak()
        val currentTime = System.currentTimeMillis()

        if (currentStreak == null) {
            // Если запись отсутствует, создаем новую с 1 днем
            streakDao.insertStreak(Streak(id = 0, days = 1, lastLoginDate = currentTime))
        } else {
            val lastLoginDate = currentStreak.lastLoginDate
            val calendar = Calendar.getInstance()

            // Проверяем, был ли вход сегодня
            calendar.timeInMillis = lastLoginDate
            val lastLoginDay = calendar.get(Calendar.DAY_OF_YEAR)
            val lastLoginYear = calendar.get(Calendar.YEAR)

            calendar.timeInMillis = currentTime
            val currentDay = calendar.get(Calendar.DAY_OF_YEAR)
            val currentYear = calendar.get(Calendar.YEAR)

            if (currentYear == lastLoginYear && currentDay == lastLoginDay) {
                // Пользователь уже заходил сегодня, ничего не делаем
                return
            } else if (currentYear == lastLoginYear && currentDay == lastLoginDay + 1) {
                // Пользователь зашел на следующий день, увеличиваем счетчик
                currentStreak.days += 1
            } else {
                // Пользователь пропустил день, сбрасываем счетчик
                currentStreak.days = 1
            }

            // Обновляем дату последнего входа
            currentStreak.lastLoginDate = currentTime
            streakDao.updateStreak(currentStreak)
        }
    }

    suspend fun resetStreak() {
        streakDao.resetStreak()
    }
}