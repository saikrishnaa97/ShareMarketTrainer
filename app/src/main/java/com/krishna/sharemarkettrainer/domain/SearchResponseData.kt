package com.krishna.sharemarkettrainer.domain

data class SearchResponseData(
    val `data`: List<ResultData>
)

data class ResultData(
    val name: String,
    val symbol: String,
    val url: String
)