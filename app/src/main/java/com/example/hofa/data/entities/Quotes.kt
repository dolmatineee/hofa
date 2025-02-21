package com.example.hofa.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quotes(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val quote: String
)
