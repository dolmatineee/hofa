package com.example.hofa.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streak")
data class Streak(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var days: Int,
    var lastLoginDate: Long
)
