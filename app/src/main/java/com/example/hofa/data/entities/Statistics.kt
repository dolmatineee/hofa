package com.example.hofa.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "statistics")
data class Statistics(
    @PrimaryKey val date: String, // Дата будет уникальным ключом
    val sessionsByHour: String, // Храним Map<Int, Int> как строку (JSON)
    val targetSessions: Int,
    val totalSessions: Int,
    val totalRounds: Int,
    val totalDuration: Int,
    val totalBreaths: Int,
    val totalBreathHoldDuration: Int
) {
    // Метод для преобразования Map<Int, Int> в JSON-строку
    companion object {
        fun fromMap(map: Map<Int, Int>): String {
            return Gson().toJson(map)
        }
    }

    // Метод для преобразования JSON-строки обратно в Map<Int, Int>
    fun toMap(): Map<Int, Int> {
        val type = object : TypeToken<Map<Int, Int>>() {}.type
        return Gson().fromJson(sessionsByHour, type)
    }
}