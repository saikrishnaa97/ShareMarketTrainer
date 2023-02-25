package com.krishna.sharemarkettrainer.domain

data class TopChangersData(
    val `data`: List<ChangersData>,
    val totVal: Double,
    val totVol: Int
)

data class ChangersData(
    val identifier: String,
    val lastPrice: Double,
    val pChange: Double,
    val symbol: String,
    val totalTradedValue: Double,
    val totalTradedVolume: Int
)