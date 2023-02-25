package com.krishna.sharemarkettrainer.domain

data class NWeekChangeData(
    val date: String,
    val numberOfWeeks: Int,
    val price: Double,
    val symbol: String,
    val type: String
)