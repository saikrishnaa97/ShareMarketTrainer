package com.krishna.sharemarkettrainer.domain

data class IndexData(
    val `data`: List<IndexStockData>,
    val index: String
)

data class IndexStockData(
    val `52WkHigh`: Double,
    val `52WkLow`: Double,
    val dayHigh: Double,
    val dayLow: Double,
    val ltp: Double,
    val name: String,
    val symbol: String
)