package com.github.ibaykoc.kdatasample.data

import com.github.ibaykoc.kdataannotation.KData

@KData
data class ListSimpleRawData(
    @KData.Field
    val username: String?,
    @KData.Field
    val comments: List<String?>?,
    @KData.ParentField
    val repositories: List<Repository?>?
) {
    data class Repository(
        @KData.Field
        val name: String?,
        @KData.Field
        val star: Int?
    )
}