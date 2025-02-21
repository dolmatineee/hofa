package com.example.hofa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hofa.data.entities.Settings
import com.example.hofa.data.repository.QuotesRepository
import com.example.hofa.data.repository.SettingsRepository
import com.example.hofa.data.repository.StreakRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {


    // Состояние для хранения настроек
    private val _settings = MutableStateFlow<Settings?>(null)
    val settings: StateFlow<Settings?> get() = _settings



    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading






    fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { newSettings ->
                _settings.value = newSettings
                _isLoading.value = false
            }
        }
    }

    // Обновление настроек
    fun updateSettings(newSettings: Settings) {
        viewModelScope.launch {
            try {
                settingsRepository.updateSettings(newSettings)
                _settings.value = newSettings
            } catch (_: Exception) {

            }
        }
    }
}