package com.example.hofa.data.entities

data class ChartData(
    val sessionsByHour: Map<Int, Int>,
    val yAxisLabels: List<Int>,
    val xAxisLabels: List<Int>
)