package com.example.hofa.di

import android.content.Context
import com.example.hofa.data.dao.QuotesDao
import com.example.hofa.data.dao.SettingsDao
import com.example.hofa.data.dao.StatisticsDao
import com.example.hofa.data.dao.StreakDao
import com.example.hofa.data.database.AppDatabase
import com.example.hofa.data.repository.QuotesRepository
import com.example.hofa.data.repository.SettingsRepository
import com.example.hofa.data.repository.StatisticsRepository
import com.example.hofa.data.repository.StreakRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.create(context)
    }

    @Provides
    @Singleton
    fun provideQuotesDao(database: AppDatabase): QuotesDao {
        return database.quotesDao()
    }

    @Provides
    @Singleton
    fun provideStreakDao(database: AppDatabase): StreakDao {
        return database.streakDao()
    }

    @Provides
    @Singleton
    fun provideSettingsDao(database: AppDatabase): SettingsDao {
        return database.settingsDao()
    }

    @Provides
    @Singleton
    fun provideStatisticsDao(database: AppDatabase): StatisticsDao {
        return database.statisticsDao()
    }



    @Provides
    @Singleton
    fun provideQuotesRepository(
        quotesDao: QuotesDao,
    ): QuotesRepository {
        return QuotesRepository(quotesDao)
    }

    @Provides
    @Singleton
    fun provideStreakRepository(
        streakDao: StreakDao,
    ): StreakRepository {
        return StreakRepository(streakDao)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        settingsDao: SettingsDao,
    ): SettingsRepository {
        return SettingsRepository(settingsDao)
    }

    @Provides
    @Singleton
    fun provideStatisticsRepository(statisticsDao: StatisticsDao): StatisticsRepository {
        return StatisticsRepository(statisticsDao)
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }
}