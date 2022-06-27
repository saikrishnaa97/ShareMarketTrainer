package com.krishna.sharemarkettrainer

class StockData{
    private var uid: String = ""
    private var stockSymbol: String = ""
    private var purchasedAt: Double = 0.0
    private var status: String = ""
    private var soldAt: Double = -1.0
    private var exchange: String = ""
    private var numOfShares: Int = 0

    constructor()

    constructor(
        uid: String,
        stockSymbol: String,
        purchasedAt: Double,
        status: String,
        soldAt: Double,
        exchange: String,
        numOfShares: Int
    ) {
        this.uid = uid
        this.stockSymbol = stockSymbol
        this.purchasedAt = purchasedAt
        this.status = status
        this.exchange = exchange
        this.soldAt = soldAt
        this.numOfShares = numOfShares
    }



}
