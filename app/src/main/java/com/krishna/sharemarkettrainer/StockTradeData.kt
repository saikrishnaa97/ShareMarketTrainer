package com.krishna.sharemarkettrainer

class StockTradeData{
    var stockTradeDataList: List<StockTradeDataItem>? = null

    constructor()

    constructor(
    stockTradeDataList: List<StockTradeDataItem>
) {
    this.stockTradeDataList = stockTradeDataList
}

}
class StockTradeDataItem {
    var exchange: String = ""
    var numOfShares: Int = 0
    var purchasedAt: Int = 0
    var soldAt: Int = 0
    var status: String = ""
    var stockSymbol: String = ""
    var uid: String = ""

    constructor()

    constructor(
        exchange: String,
        numOfShares: Int,
        purchasedAt: Int,
        soldAt: Int,
        status: String,
        stockSymbol: String,
        uid: String
    ) {
        this.exchange = exchange
        this.numOfShares = numOfShares
        this.purchasedAt = purchasedAt
        this.soldAt = soldAt
        this.status = exchange
        this.stockSymbol = stockSymbol
        this.uid = uid
    }

}