package com.example.hofa.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startTime: String,
    val duration: Int,
    val rounds: Int,
    val breaths: Int,
    val breathHoldDuration: Int
)