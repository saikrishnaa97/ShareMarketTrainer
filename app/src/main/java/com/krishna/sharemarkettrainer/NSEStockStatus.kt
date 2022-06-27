package com.krishna.sharemarkettrainer

data class NSEStockStatus(
    val industryInfo: IndustryInfo,
    val info: Info,
    val metadata: Metadata,
    val preOpenMarket: PreOpenMarket,
    val priceInfo: PriceInfo,
    val securityInfo: SecurityInfo
)
data class Ato(
    val buy: Int,
    val sell: Int
)
data class IndustryInfo(
    val basicIndustry: String,
    val industry: String,
    val macro: String,
    val sector: String
)
data class Info(
    val activeSeries: List<Any>,
    val companyName: String,
    val debtSeries: List<String>,
    val identifier: String,
    val isCASec: Boolean,
    val isDebtSec: Boolean,
    val isDelisted: Boolean,
    val isETFSec: Boolean,
    val isFNOSec: Boolean,
    val isSLBSec: Boolean,
    val isSuspended: Boolean,
    val isTop10: Boolean,
    val isin: String,
    val symbol: String,
    val tempSuspendedSeries: List<String>
)
data class IntraDayHighLow(
    val max: Int,
    val min: Int,
    val value: Int
)
data class Metadata(
    val industry: String,
    val isin: String,
    val lastUpdateTime: String,
    val listingDate: String,
    val pdSectorInd: String,
    val pdSectorPe: String,
    val pdSymbolPe: String,
    val series: String,
    val status: String,
    val symbol: String
)
data class Preopen(
    val iep: Boolean
)
data class PreOpenMarket(
    val ato: Ato,
    val preopen: List<Preopen>
)
data class PriceInfo(
    val basePrice: Int,
    val change: Int,
    val close: Int,
    val intraDayHighLow: IntraDayHighLow,
    val lastPrice: Int,
    val lowerCP: String,
    val `open`: Int,
    val pChange: Int,
    val pPriceBand: String,
    val previousClose: Int,
    val upperCP: String,
    val vwap: Int,
    val weekHighLow: WeekHighLow
)
data class SecurityInfo(
    val boardStatus: String,
    val classOfShare: String,
    val derivatives: String,
    val faceValue: Int,
    val issuedSize: String,
    val sessionNo: String,
    val slb: String,
    val surveillance: String,
    val tradingSegment: String,
    val tradingStatus: String
)
data class WeekHighLow(
    val max: Int,
    val maxDate: Any,
    val min: Int,
    val minDate: Any,
    val value: Int
)