package com.github.ibaykoc.kdatasample.data

import com.github.ibaykoc.kdataannotation.KData

@KData
data class SimpleRawData(
    @KData.Field(allowNull = true)
    val status: Boolean?,
    @KData.Field
    val first_name: String?,
    @KData.Field
    val last_name: String?,
    val last_update: String
)