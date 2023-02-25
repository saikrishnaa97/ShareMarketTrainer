package com.krishna.sharemarkettrainer.domain

data class ScannerData(
    val `data`: List<Data>,
    val list: String
)

data class Data(
    val `52WkHigh`: Int,
    val curHigh: Int,
    val ltp: Double,
    val name: String,
    val symbol: String
)