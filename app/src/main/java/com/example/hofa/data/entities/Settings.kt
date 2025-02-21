package com.example.hofa.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,  // Уникальный идентификатор
    val preparationTime: Int = 30,  // Время подготовки в секундах
    val rounds: Int = 3,           // Количество раундов
    val breathingSpeed: String = "Обычная", // Скорость дыхания
    val breathsCount: Int = 30,     // Количество вдохов
    val breathHoldDuration: Int = 15, // Продолжительность задержки дыхания
    val accompaniment: String = "Дыхание и голос", // Сопровождение
    val backgroundSound: String = "Утренние трели", // Фоновый звук
    val soundVolume: Int = 20,      // Громкость звука
    val voice: String = "Мужской"         // Голос
)