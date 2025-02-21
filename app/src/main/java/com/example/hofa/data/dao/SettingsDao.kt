package com.example.hofa.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hofa.data.entities.Settings
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    // Вставка настроек
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: Settings)

    // Обновление настроек
    @Update
    suspend fun updateSettings(settings: Settings)

    @Query("SELECT * FROM settings WHERE id = 1")
    fun getSettings(): Flow<Settings>

    // Получение всех настроек (если нужно)
    @Query("SELECT * FROM settings")
    fun getAllSettings(): LiveData<List<Settings>>
}