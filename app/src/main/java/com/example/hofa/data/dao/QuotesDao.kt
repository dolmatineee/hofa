package com.example.hofa.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.hofa.data.entities.Quotes

@Dao
interface QuotesDao {

    @Query("SELECT * FROM quotes ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomQuote(): Quotes
}