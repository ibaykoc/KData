package com.github.ibaykoc.kdatasample.data

import com.github.ibaykoc.kdataannotation.KData

@KData
data class NestedRawData(
    @KData.ParentField
    val account: Account?,
    val last_update: String?
) {
    data class Account(
        @KData.Field(allowNull = true)
        val status: Boolean?,
        @KData.Field
        val first_name: String?,
        @KData.Field
        val last_name: String?
    )
}