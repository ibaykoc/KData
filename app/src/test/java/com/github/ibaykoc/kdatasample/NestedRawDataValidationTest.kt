package com.github.ibaykoc.kdatasample

import com.github.ibaykoc.kdataannotation.KData
import org.junit.Assert
import org.junit.Test

class NestedRawDataValidationTest {

    @KData
    data class NestedRawData(
        @KData.ParentField
        val account: Account?,
        val last_update: String?
    )

    data class Account(
        @KData.Field(allowNull = true)
        val status: Boolean?,
        @KData.Field
        val first_name: String?,
        @KData.Field
        val last_name: String?
    )

    @Test
    fun `Test simple raw data valid`() {
        val nestedRawData = NestedRawData(
            account = Account(
                status = null,
                first_name = "Jane",
                last_name = "Doe"
            ),
            last_update = "01-01-2001 : 00:00"
        )
        println(nestedRawData.validate())
        val expected = NestedRawDataValidated(
            first_nameValidated = "Jane",
            last_nameValidated = "Doe"
        )

        Assert.assertEquals(expected, nestedRawData.validate())
    }

    @Test
    fun `Test simple raw data not valid`() {
        val nestedRawData = NestedRawData(
            account = Account(
                status = null,
                first_name = null,
                last_name = "Doe"
            ),
            last_update = "01-01-2001 : 00:00"
        )
        val nestedRawData2 = NestedRawData(
            account = Account(
                status = null,
                first_name = "Jane",
                last_name = null
            ),
            last_update = "01-01-2001 : 00:00"
        )
        val nestedRawData3 = NestedRawData(
            account = Account(
                status = null,
                first_name = null,
                last_name = null
            ),
            last_update = "01-01-2001 : 00:00"
        )
        val expected = null

        Assert.assertEquals(expected, nestedRawData.validate())
        Assert.assertEquals(expected, nestedRawData2.validate())
        Assert.assertEquals(expected, nestedRawData3.validate())
    }

    @Test
    fun `Test simple raw data allow null`() {
        val nestedRawData = NestedRawData(
            account = Account(
                status = null,
                first_name = "Jane",
                last_name = "Doe"
            ),
            last_update = "01-01-2001 : 00:00"
        )
        val expected = NestedRawDataValidated(
            statusValidated = null,
            first_nameValidated = "Jane",
            last_nameValidated = "Doe"
        )
        val nestedRawData2 = NestedRawData(
            account = Account(
                status = true,
                first_name = "Jane",
                last_name = "Doe"
            ),
            last_update = "01-01-2001 : 00:00"
        )
        val expected2 = NestedRawDataValidated(
            statusValidated = true,
            first_nameValidated = "Jane",
            last_nameValidated = "Doe"
        )
        Assert.assertEquals(expected, nestedRawData.validate())
        Assert.assertEquals(expected2, nestedRawData2.validate())
    }
}