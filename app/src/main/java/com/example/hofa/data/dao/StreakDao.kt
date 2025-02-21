package com.example.hofa.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.hofa.data.entities.Streak

@Dao
interface StreakDao {

    @Insert
    suspend fun insertStreak(streak: Streak)

    @Update
    suspend fun updateStreak(streak: Streak)

    @Query("SELECT * FROM streak LIMIT 1")
    suspend fun getStreak(): Streak

    @Query("DELETE FROM streak")
    suspend fun resetStreak()
}