package com.example.hofa.data.entities

// Состояния медитации
sealed class MeditationState {
    object Idle : MeditationState()
    data class Preparation(val totalTime: Int) : MeditationState()
    data class Breathing(val round: Int, val breathsCount: Int) : MeditationState()
    data class DeepExhale(val round: Int) : MeditationState() // Новое состояние
    data class BreathHoldAfterExhale(val round: Int, val holdDuration: Int) : MeditationState()
    data class DeepInhale(val round: Int) : MeditationState()
    data class BreathHoldAfterInhale(val round: Int, val holdDuration: Int) : MeditationState()
    data class Paused(val currentState: MeditationState) : MeditationState()
    object Finished : MeditationState()
}