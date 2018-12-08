package com.github.ibaykoc.kdatasample.data

import com.github.ibaykoc.kdataannotation.KRawData

@KRawData("LoginValid")
data class LoginResponse (
    val status: Boolean?,
    @KRawData.ValidateParentField
    val account: Account?,
    val last_login: String?
)

data class Account(
    @KRawData.ValidateField("firstName")
    val first_name: String?,

    @KRawData.ValidateField("lastName")
    val last_name: String?
)