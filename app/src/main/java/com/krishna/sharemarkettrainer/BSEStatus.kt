package com.krishna.sharemarkettrainer

class BSEStatus : ArrayList<BSEStatusItem>()

data class BSEStatusItem(
    val F: String,
    val High: String,
    val I_open: String,
    val Low: String,
    val Prev_Close: String,
    val chg: String,
    val dttm: String,
    val indxnm: String,
    val istream: String,
    val ltp: String,
    val msg: String,
    val perchg: String,
    val source: String
)