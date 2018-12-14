package com.github.ibaykoc.kdatasample.data

import com.github.ibaykoc.kdataannotation.KData

@KData
data class MultiNestedRawData(
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
        val last_name: String?,
        @KData.ParentField
        val dob: DOB?
    )

    data class DOB(
        @KData.Field
        val day: Int?,
        @KData.Field(allowNull = true)
        val month: Int?,
        @KData.Field(allowNull = true)
        val year: Int?
    )
}