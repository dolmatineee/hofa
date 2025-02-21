package com.example.hofa.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.hofa.data.entities.Goal
import com.example.hofa.data.entities.Session
import com.example.hofa.data.entities.Statistics
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticsDao {
    @Insert
    suspend fun insertStatistics(statistics: Statistics)

    @Update
    suspend fun updateStatistics(statistics: Statistics)

    @Query("SELECT * FROM statistics WHERE date = :date")
    suspend fun getStatisticsByDate(date: String): Statistics?

    @Query("SELECT * FROM statistics WHERE date = :date")
    fun getStatisticsByDateLiveData(date: String): Flow<Statistics?>

    @Query("SELECT * FROM statistics ORDER BY date DESC")
    fun getAllStatistics(): LiveData<List<Statistics>>

    @Query("DELETE FROM statistics WHERE date = :date")
    suspend fun deleteStatisticsByDate(date: String)

    @Insert
    suspend fun insertGoal(goal: Goal)

    @Query("SELECT * FROM goals ORDER BY id DESC LIMIT 1")
    suspend fun getLatestGoal(): Goal

    @Update
    suspend fun updateGoal(goal: Goal)
}