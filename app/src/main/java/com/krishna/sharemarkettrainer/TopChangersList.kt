package com.krishna.sharemarkettrainer

data class TopChangersList(
    val Table: List<Table>
)

data class Table(
    val GUJ_SCRIPNAME: String,
    val GUJ_SCRIP_ID: String,
    val HIN_SCRIPNAME: String,
    val HIN_SCRIP_ID: String,
    val LONGNAME: String,
    val Ltradert: Double,
    val MAR_SCRIPNAME: String,
    val MAR_SCRIP_ID: String,
    val NSUrl: String,
    val ScripName: String,
    val SrNo: Int,
    val change_percent: Double,
    val change_val: Double,
    val scrip_cd: Int,
    val scrip_id: String
)