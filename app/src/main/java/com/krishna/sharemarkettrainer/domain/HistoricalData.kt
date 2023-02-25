package com.krishna.sharemarkettrainer.domain

data class HistoricalData(
    val `data`: List<HistoryData>,
    val symbol: String
)
data class HistoryData(
    val closingPrice: Double,
    val date: String,
    val dayHigh: Double,
    val dayLow: Double,
    val openingPrice: Double
)