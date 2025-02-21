package com.example.hofa.data.repository

import com.example.hofa.data.dao.QuotesDao
import com.example.hofa.data.entities.Quotes

class QuotesRepository(private val quotesDao: QuotesDao) {

    suspend fun getRandomQuote(): Quotes {
        return quotesDao.getRandomQuote()
    }
}