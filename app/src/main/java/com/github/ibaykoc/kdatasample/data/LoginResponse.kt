package com.github.ibaykoc.kdatasample.data

import com.github.ibaykoc.kdataannotation.KData

@KData("LoginValid")
data class LoginResponse (
    val status: Boolean?,
    @KData.ParentField
    val account: Account?,
    val last_login: String?
)

data class Account(
    @KData.Field("firstName")
    val first_name: String?,

    @KData.Field("lastName", allowNull = true)
    val last_name: String?
)