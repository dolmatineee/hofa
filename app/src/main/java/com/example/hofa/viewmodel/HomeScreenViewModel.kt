package com.example.hofa.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hofa.data.repository.QuotesRepository
import com.example.hofa.data.repository.StreakRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor (
    private val quotesRepository: QuotesRepository,
    private val streakRepository: StreakRepository,
): ViewModel() {

    private val _days = MutableStateFlow(0)
    val days: StateFlow<Int> get() = _days

    private val _quote = MutableStateFlow("")
    val quote: StateFlow<String> get() = _quote



    init {
        loadData()
    }

    private fun loadData() {

        viewModelScope.launch {
            streakRepository.updateStreak()
            val days = streakRepository.getStreak().days
            _days.value = days

            val quote = quotesRepository.getRandomQuote().quote
            _quote.value = quote
        }

    }
}