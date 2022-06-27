package com.krishna.sharemarkettrainer

data class NSEStatus(
    val code: Int,
    val corrClose: Int,
    val corrOpen: Int,
    val data: List<NSEData>,
    val haltedStatus: String,
    val mktClose: Int,
    val mktOpen: Int,
    val preClose: Int,
    val preOpen: Int,
    val status: String,
    val time: String
)

data class NSEData(
    val change: String,
    val imgFileName: String,
    val lastPrice: String,
    val name: String,
    val pChange: String
)