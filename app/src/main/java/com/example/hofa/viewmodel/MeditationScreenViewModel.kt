package com.example.hofa.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hofa.data.entities.MeditationState
import com.example.hofa.data.entities.Session
import com.example.hofa.data.entities.Settings
import com.example.hofa.data.entities.Statistics
import com.example.hofa.data.repository.SettingsRepository
import com.example.hofa.data.repository.StatisticsRepository
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MeditationScreenViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val statisticsRepository: StatisticsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val backgroundPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
        }
    }

    private val voicePlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build()
    }

    private val soundEffectsPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build()
    }

    private val _statistics = MutableStateFlow<Statistics?>(null)
    val statistics: StateFlow<Statistics?> get() = _statistics

    private var currentSession: Session? = null

    private val _settings = MutableStateFlow<Settings>(Settings())
    val settings: StateFlow<Settings> get() = _settings

    private val _state = MutableStateFlow<MeditationState>(MeditationState.Idle)
    val state: StateFlow<MeditationState> = _state

    private var isMeditationPaused = false

    private val _timeLeft = MutableStateFlow(0)
    val timeLeft: StateFlow<Int> = _timeLeft

    private var isSkipping = false
    private var isMeditationActive = false // Флаг для управления таймером


    fun pauseMeditation() {
        if (_state.value is MeditationState.Paused) return

        isMeditationPaused = true
        isMeditationActive = false // Остановка таймера
        backgroundPlayer.pause()
        voicePlayer.pause()
        soundEffectsPlayer.pause()

        _state.value = MeditationState.Paused(_state.value)
    }

    fun resumeMeditation() {
        if (_state.value !is MeditationState.Paused) return

        isMeditationPaused = false
        isMeditationActive = true // Возобновление таймера
        backgroundPlayer.play()
        voicePlayer.play()
        soundEffectsPlayer.play()

        val pausedState = (_state.value as MeditationState.Paused).currentState
        _state.value = pausedState
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun finishMeditation() {
        viewModelScope.launch {
            isMeditationActive = false // Остановка таймера
            currentSession?.let { session ->
                statisticsRepository.updateStatistics(session)
                val stats = statisticsRepository.getStatisticsForDate(LocalDate.now().toString()).first()
                _statistics.value = stats
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startMeditation() {
        viewModelScope.launch {
            // Инициализация текущей сессии
            currentSession = Session(
                startTime = LocalDateTime.now().toString(),
                duration = 0,
                rounds = 0,
                breaths = 0,
                breathHoldDuration = 0
            )

            playBackgroundSound(settings.value.backgroundSound, settings.value.soundVolume)

            // Запуск таймера для увеличения продолжительности медитации
            isMeditationActive = true
            launch {
                while (isMeditationActive) {
                    delay(1000) // Задержка в 1 секунду
                    currentSession = currentSession?.copy(
                        duration = currentSession!!.duration + 1
                    )
                }
            }

            startPreparation()
        }
    }

    fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { newSettings ->
                _settings.value = newSettings
            }
        }
    }

    private fun playBackgroundSound(backgroundSound: String, volume: Int) {
        val soundUri = when (backgroundSound) {
            "Утренние трели" -> "android.resource://${context.packageName}/raw/sound_birds"
            "Шёпот ветра" -> "android.resource://${context.packageName}/raw/sound_wind"
            "Журчание ручья" -> "android.resource://${context.packageName}/raw/sound_water"
            "Вечерние цикады" -> "android.resource://${context.packageName}/raw/sound_cicadas"
            "Живой лес" -> "android.resource://${context.packageName}/raw/sound_forest"
            else -> null
        }

        soundUri?.let { uri ->
            val mediaItem = MediaItem.fromUri(Uri.parse(uri))
            backgroundPlayer.setMediaItem(mediaItem)
            backgroundPlayer.prepare()
            backgroundPlayer.play()

            // Установка громкости
            val normalizedVolume = volume / 100f
            backgroundPlayer.volume = normalizedVolume

            Log.d(
                "MeditationScreenViewModel",
                "Playing background sound: $uri at volume $normalizedVolume"
            )
        } ?: run {
            Log.e("MeditationScreenViewModel", "Background sound URI is null for: $backgroundSound")
        }
    }


    // Этап подготовки
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun startPreparation() {



        _state.value = MeditationState.Preparation(settings.value.preparationTime)
        _timeLeft.value = settings.value.preparationTime



        for (i in settings.value.preparationTime downTo 0) {
            if (isSkipping) {
                isSkipping = false
                break
            }
            while (isMeditationPaused) {
                delay(100) // Короткая задержка, чтобы не нагружать процессор
            }
            delay(1000)
            _timeLeft.value = i

        }

        if (!isSkipping) {
            startRound(1)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun startRound(round: Int) {
        if (round > settings.value.rounds) {
            _state.value = MeditationState.Finished
            backgroundPlayer.stop()
            voicePlayer.stop()
            return
        }

        // Обновляем количество раундов
        currentSession = currentSession?.copy(
            rounds = round
        )

        // Этап вдохов
        startBreathing(round)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun startBreathing(round: Int) {


        _state.value = MeditationState.Breathing(round, settings.value.breathsCount)
        _timeLeft.value = settings.value.breathsCount

        for (i in settings.value.breathsCount downTo 1) {
            if (isSkipping) {
                isSkipping = false
                break
            }

            // Проверяем, находится ли медитация на паузе
            while (isMeditationPaused) {
                delay(100) // Короткая задержка, чтобы не нагружать процессор
            }

            _timeLeft.value = i
            playInhaleSound()
            delay(getBreathingDelay())

            // Проверяем паузу снова перед следующим действием
            while (isMeditationPaused) {
                delay(100)
            }

            playExhaleSound()
            delay(getBreathingDelay())

            // Обновляем количество вдохов, если медитация не на паузе
            if (!isMeditationPaused) {
                currentSession = currentSession?.copy(
                    breaths = currentSession!!.breaths + 1
                )
            }


        }

        if (!isSkipping) {
            startDeepExhale(round)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun startDeepExhale(round: Int) {
        _state.value = MeditationState.DeepExhale(round)

        if (shouldPlayVoice()) {
            playVoicePhrase("Сделайте глубокий выдох")
        }

        // Ожидание 4 секунд с учетом паузы
        val delayTime = 4000L
        val startTime = System.currentTimeMillis()
        var elapsedTime = 0L

        while (elapsedTime < delayTime) {
            if (isMeditationPaused) {
                // Если медитация на паузе, ждем снятия паузы
                delay(100) // Короткая задержка, чтобы не нагружать процессор
            } else {
                // Если медитация не на паузе, продолжаем отсчет времени
                delay(100)
                elapsedTime = System.currentTimeMillis() - startTime
            }
        }

        // Переход к следующему этапу, если медитация не на паузе
        if (!isMeditationPaused) {
            startBreathHoldAfterExhale(round)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun startBreathHoldAfterExhale(round: Int) {


        _state.value = MeditationState.BreathHoldAfterExhale(round, settings.value.breathHoldDuration)
        _timeLeft.value = settings.value.breathHoldDuration

        if (shouldPlayVoice()) {
            playVoicePhrase("Задержите дыхание")
        }

        // Запуск отсчета с учетом паузы
        countDownWithPause(settings.value.breathHoldDuration) { remaining ->
            _timeLeft.value = remaining
            if (!isMeditationPaused) { // Обновляем длительность задержки дыхания только если не на паузе
                currentSession = currentSession?.copy(
                    breathHoldDuration = currentSession!!.breathHoldDuration + 1
                )
            }
        }

        if (!isSkipping) {
            startDeepInhale(round)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun startDeepInhale(round: Int) {
        _state.value = MeditationState.DeepInhale(round)

        if (shouldPlayVoice()) {
            playVoicePhrase("Сделайте глубокий вдох")
        }

        // Ожидание 4 секунд с учетом паузы
        val delayTime = 4000L
        val startTime = System.currentTimeMillis()
        var elapsedTime = 0L

        while (elapsedTime < delayTime) {
            if (isMeditationPaused) {
                // Если медитация на паузе, ждем снятия паузы
                delay(100) // Короткая задержка, чтобы не нагружать процессор
            } else {
                // Если медитация не на паузе, продолжаем отсчет времени
                delay(100)
                elapsedTime = System.currentTimeMillis() - startTime
            }
        }

        // Переход к следующему этапу, если медитация не на паузе
        if (!isMeditationPaused) {
            startBreathHoldAfterInhale(round)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun startBreathHoldAfterInhale(round: Int) {


        _state.value = MeditationState.BreathHoldAfterInhale(round, settings.value.breathHoldDuration)
        _timeLeft.value = settings.value.breathHoldDuration

        if (shouldPlayVoice()) {
            playVoicePhrase("Задержите дыхание")
        }

        // Запуск отсчета с учетом паузы
        countDownWithPause(settings.value.breathHoldDuration) { remaining ->
            _timeLeft.value = remaining
            if (!isMeditationPaused) { // Обновляем длительность задержки дыхания только если не на паузе
                currentSession = currentSession?.copy(
                    breathHoldDuration = currentSession!!.breathHoldDuration + 1
                )
            }
        }
        if (!isSkipping) {
            startRound(round + 1) // Переход к следующему раунду
        }
    }


    private suspend fun countDownWithPause(time: Int, onTick: (Int) -> Unit) {
        for (i in time downTo 1) {
            if (isSkipping) {
                isSkipping = false
                break
            }

            // Проверяем, находится ли медитация на паузе
            while (isMeditationPaused) {
                delay(100) // Короткая задержка, чтобы не нагружать процессор
            }

            onTick(i) // Обновляем оставшееся время
            delay(1000) // Задержка в 1 секунду
        }
    }

    fun skipStage() {
        isSkipping = true
    }


    override fun onCleared() {
        super.onCleared()
        backgroundPlayer.release() // Освободить ресурсы плеера
        voicePlayer.release() // Освободить ресурсы плеера
    }


    private fun playInhaleSound() {
        if (settings.value.accompaniment != "Ничего") {
            val soundUri = "android.resource://${context.packageName}/raw/sound_inhale"
            playSound(soundEffectsPlayer, soundUri)
        }
    }

    private fun playExhaleSound() {
        if (settings.value.accompaniment != "Ничего") {
            val soundUri = "android.resource://${context.packageName}/raw/sound_exhale"
            playSound(soundEffectsPlayer, soundUri)
        }
    }

    private fun playVoicePhrase(phrase: String) {
        val soundUri = when (phrase) {
            "Вдох" -> if (settings.value.voice == "Мужской") {
                "android.resource://${context.packageName}/raw/inhale_man"
            } else {
                "android.resource://${context.packageName}/raw/inhale_woman"
            }

            "Выдох" -> if (settings.value.voice == "Мужской") {
                "android.resource://${context.packageName}/raw/exhale_man"
            } else {
                "android.resource://${context.packageName}/raw/exhale_woman"
            }

            "Сделайте глубокий вдох" -> if (settings.value.voice == "Мужской") {
                "android.resource://${context.packageName}/raw/deep_inhale_man"
            } else {
                "android.resource://${context.packageName}/raw/deep_inhale_woman"
            }

            "Сделайте глубокий выдох" -> if (settings.value.voice == "Мужской") {
                "android.resource://${context.packageName}/raw/deep_exhale_man"
            } else {
                "android.resource://${context.packageName}/raw/deep_exhale_woman"
            }

            "Задержите дыхание" -> if (settings.value.voice == "Мужской") {
                "android.resource://${context.packageName}/raw/holding_breath_man"
            } else {
                "android.resource://${context.packageName}/raw/holding_breath_woman"
            }

            else -> null
        }

        soundUri?.let { uri ->
            val mediaItem = MediaItem.fromUri(Uri.parse(uri))
            voicePlayer.volume = 0.5f
            voicePlayer.setMediaItem(mediaItem)
            voicePlayer.prepare()
            voicePlayer.play()
            voicePlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == ExoPlayer.STATE_ENDED) {
                        voicePlayer.clearMediaItems()
                    }
                }
            })
        }
    }

    private fun playSound(player: ExoPlayer, uri: String) {
        val mediaItem = MediaItem.fromUri(Uri.parse(uri))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    private fun getBreathingDelay(): Long {
        return when (settings.value.breathingSpeed) {
            "Быстрая" -> 1000L // 1 секунда
            "Обычная" -> 2000L // 2 секунды
            "Медленная" -> 3000L // 3 секунды
            else -> 2000L // По умолчанию
        }
    }

    private fun shouldPlayVoice(): Boolean {
        return settings.value.accompaniment == "Дыхание и голос"
    }

}



