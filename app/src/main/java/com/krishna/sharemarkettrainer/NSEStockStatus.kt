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
    val max: Double,
    val min: Double,
    val value: Double
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
    val basePrice: Double,
    val change: Double,
    val close: Double,
    val intraDayHighLow: IntraDayHighLow,
    val lastPrice: Double,
    val lowerCP: String,
    val open: Double,
    val pChange: Double,
    val pPriceBand: String,
    val previousClose: Double,
    val upperCP: String,
    val vwap: Double,
    val weekHighLow: WeekHighLow
)
data class SecurityInfo(
    val boardStatus: String,
    val classOfShare: String,
    val derivatives: String,
    val faceValue: Double,
    val issuedSize: String,
    val sessionNo: String,
    val slb: String,
    val surveillance: String,
    val tradingSegment: String,
    val tradingStatus: String
)
data class WeekHighLow(
    val max: Double,
    val maxDate: Any,
    val min: Double,
    val minDate: Any,
    val value: Double
)