package com.github.ibaykoc.kdatasample

import com.github.ibaykoc.kdatasample.data.Account
import com.github.ibaykoc.kdatasample.data.LoginResponse
import com.github.ibaykoc.kdatasample.data.LoginValid
import com.github.ibaykoc.kdatasample.data.validate
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class DataValidationTest {
    @Test
    fun `Given raw data is valid, when validate, should return valid data`() {
        val loginResponse1 = LoginResponse(true, Account("Jane", "Doe"), "01-01-01 00:00")
        val expected1 = LoginValid("Jane", "Doe")

        val loginResponse2 = LoginResponse(null, Account("Jane", "Doe"), null)
        val expected2 = LoginValid("Jane", "Doe")

        Assert.assertEquals(expected1, loginResponse1.validate())
        Assert.assertEquals(expected2, loginResponse2.validate())
    }

    @Test
    fun `Given raw data is not valid, when validate, should return null`() {
        val loginResponse1 = LoginResponse(true, Account("Jane", null), "01-01-01 00:00")
        val loginResponse2 = LoginResponse(null, Account(null, "Doe"), null)

        Assert.assertEquals(null, loginResponse1.validate())
        Assert.assertEquals(null, loginResponse2.validate())
    }
}
