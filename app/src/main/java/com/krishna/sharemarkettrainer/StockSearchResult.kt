package com.krishna.sharemarkettrainer

data class StockSearchResult(
    val mfsymbols: List<Any>,
    val search_content: List<SearchContent>,
    val sitemap: List<Any>,
    val symbols: List<Symbol>
)

data class SearchContent(
    val author: String,
    val content: String,
    val content_length: Int,
    val content_type: String,
    val date: String,
    val language: String,
    val result_sub_type: String,
    val result_type: String,
    val title: String,
    val url: String
)

data class Symbol(
    val activeSeries: List<String>,
    val result_sub_type: String,
    val result_type: String,
    val symbol: String,
    val symbol_info: String,
    val symbol_suggest: List<SymbolSuggest>,
    val url: String
)

data class SymbolSuggest(
    val input: String,
    val weight: Int
)