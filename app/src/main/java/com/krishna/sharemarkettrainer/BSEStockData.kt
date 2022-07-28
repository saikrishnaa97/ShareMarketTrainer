package com.krishna.sharemarkettrainer

data class BSEStockData(
    val CurrDate: String,
    val CurrTime: String,
    val CurrVal: String,
    val Data: String,
    val HighVal: String,
    val HighVol: String,
    val LowVal: String,
    val LowVol: String,
    val PrevClose: String,
    val Scripname: String
)