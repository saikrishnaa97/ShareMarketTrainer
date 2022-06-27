package com.krishna.sharemarkettrainer

class Users {

    private var uid: String = ""
    private var name: String = ""
    private var email: String = ""
    private var availableBalance: Int = 0

    constructor()

    constructor(
        uid: String,
        name: String,
        email: String,
        availableBalance: Int
    ) {
        this.uid = uid
        this.name = name
        this.email = email
        this.availableBalance = availableBalance
    }

    fun getUid(): String?{
        return this.uid
    }

    fun setUid(uid: String) {
        this.uid = uid
    }

    fun getName(): String?{
        return this.name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getEmail(): String?{
        return this.email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getavailableBalance(): Int?{
        return this.availableBalance
    }

    fun setAvailableBalance(availableBalance: Int) {
        this.availableBalance = availableBalance
    }

}