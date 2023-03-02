package com.krishna.sharemarkettrainer.domain

data class PortfolioData(
    val portfolio: List<Portfolio>
)

data class Portfolio(
    val avgCost: Double,
    val ltp: Double,
    val numOfShares: Int,
    val stopLoss: Double,
    val symbol: String,
    val target: Double,
    val uid: String,
    val status: String = "HELLO"
)