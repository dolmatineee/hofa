package com.example.hofa.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hofa.data.dao.QuotesDao
import com.example.hofa.data.dao.SettingsDao
import com.example.hofa.data.dao.StatisticsDao
import com.example.hofa.data.dao.StreakDao
import com.example.hofa.data.entities.Goal
import com.example.hofa.data.entities.Quotes
import com.example.hofa.data.entities.Session
import com.example.hofa.data.entities.Settings
import com.example.hofa.data.entities.Statistics
import com.example.hofa.data.entities.Streak

@Database(
    entities = [Streak::class, Quotes::class, Settings::class, Session::class, Goal::class, Statistics::class],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun quotesDao(): QuotesDao
    abstract fun streakDao(): StreakDao
    abstract fun settingsDao(): SettingsDao
    abstract fun statisticsDao(): StatisticsDao

    companion object {
        private const val DATABASE_NAME = "app_database"

        fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .createFromAsset("vimhof.db")
                .build()

        }
    }
}