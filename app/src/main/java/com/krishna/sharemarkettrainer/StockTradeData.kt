package com.krishna.sharemarkettrainer

data class StockTradeData(
    val exchange: String,
    val numOfShares: Int,
    val purchasedAt: Int,
    val soldAt: Int,
    val status: String,
    val stockSymbol: String,
    val uid: String
)