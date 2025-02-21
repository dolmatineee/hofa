package com.example.hofa.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import com.example.hofa.data.dao.SettingsDao
import com.example.hofa.data.entities.Settings
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val settingsDao: SettingsDao) {

    // Получить настройки по ID
    fun getSettings(): Flow<Settings> {
        return settingsDao.getSettings()
    }

    // Вставить или обновить настройки
    suspend fun updateSettings(settings: Settings) {
        settingsDao.updateSettings(settings)
    }

    // Получить все настройки (если нужно)
    fun getAllSettings(): Flow<List<Settings>> {
        return settingsDao.getAllSettings().asFlow()
    }
}