package com.github.ibaykoc.kdatasample

import com.github.ibaykoc.kdatasample.data.NestedRawData
import com.github.ibaykoc.kdatasample.data.NestedRawDataValid
import com.github.ibaykoc.kdatasample.data.validate
import org.junit.Assert
import org.junit.Test

class NestedRawDataValidationTest {

    @Test
    fun `Test nested raw data valid`() {
        val nestedRawData = NestedRawData(
            account = NestedRawData.Account(
                status = null,
                first_name = "Jane",
                last_name = "Doe"
            ),
            last_update = "01-01-2001 : 00:00"
        )
        println(nestedRawData.validate())
        val expected = NestedRawDataValid(
            account = NestedRawDataValid.Account(
                status = null,
                first_name = "Jane",
                last_name = "Doe"
            )
        )

        Assert.assertEquals(expected, nestedRawData.validate())
    }

    @Test
    fun `Test nested raw data not valid`() {
        val nestedRawData = NestedRawData(
            account = NestedRawData.Account(
                status = null,
                first_name = null,
                last_name = "Doe"
            ),
            last_update = "01-01-2001 : 00:00"
        )
        val nestedRawData2 = NestedRawData(
            account = NestedRawData.Account(
                status = null,
                first_name = "Jane",
                last_name = null
            ),
            last_update = "01-01-2001 : 00:00"
        )
        val nestedRawData3 = NestedRawData(
            account = NestedRawData.Account(
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
    fun `Test nested raw data allow null`() {
        val nestedRawData = NestedRawData(
            account = NestedRawData.Account(
                status = null,
                first_name = "Jane",
                last_name = "Doe"
            ),
            last_update = "01-01-2001 : 00:00"
        )
        val expected = NestedRawDataValid(
            account = NestedRawDataValid.Account(
                status = null,
                first_name = "Jane",
                last_name = "Doe"
            )
        )
        val nestedRawData2 = NestedRawData(
            account = NestedRawData.Account(
                status = true,
                first_name = "Jane",
                last_name = "Doe"
            ),
            last_update = "01-01-2001 : 00:00"
        )
        val expected2 = NestedRawDataValid(
            account = NestedRawDataValid.Account(
                status = true,
                first_name = "Jane",
                last_name = "Doe"
            )
        )
        Assert.assertEquals(expected, nestedRawData.validate())
        Assert.assertEquals(expected2, nestedRawData2.validate())
    }
}