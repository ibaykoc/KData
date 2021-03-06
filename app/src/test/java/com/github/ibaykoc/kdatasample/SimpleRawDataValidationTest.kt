package com.github.ibaykoc.kdatasample

import com.github.ibaykoc.kdatasample.data.SimpleRawData
import com.github.ibaykoc.kdatasample.data.SimpleRawDataValid
import com.github.ibaykoc.kdatasample.data.validate
import org.junit.Assert
import org.junit.Test

class SimpleRawDataValidationTest {

    @Test
    fun `Test simple raw data valid`() {
        val simpleRawData = SimpleRawData(
            status = null,
            first_name = "Jane",
            last_name = "Doe",
            last_update = "01-01-2001 : 00:00"
        )
        println(simpleRawData.validate())
        val expected = SimpleRawDataValid(
            first_name = "Jane",
            last_name = "Doe"
        )

        Assert.assertEquals(expected, simpleRawData.validate())
    }

    @Test
    fun `Test simple raw data not valid`() {
        val simpleRawData = SimpleRawData(
            status = true,
            first_name = null,
            last_name = "Doe",
            last_update = "01-01-2001 : 00:00"
        )
        val simpleRawData2 = SimpleRawData(
            status = true,
            first_name = "Jane",
            last_name = null,
            last_update = "01-01-2001 : 00:00"
        )
        val simpleRawData3 = SimpleRawData(
            status = true,
            first_name = null,
            last_name = null,
            last_update = "01-01-2001 : 00:00"
        )
        val expected = null

        Assert.assertEquals(expected, simpleRawData.validate())
        Assert.assertEquals(expected, simpleRawData2.validate())
        Assert.assertEquals(expected, simpleRawData3.validate())
    }

    @Test
    fun `Test simple raw data allow null`() {
        val simpleRawData = SimpleRawData(
            status = null,
            first_name = "Jane",
            last_name = "Doe",
            last_update = "01-01-2001 : 00:00"
        )
        val expected = SimpleRawDataValid(
            status = null,
            first_name = "Jane",
            last_name = "Doe"
        )
        val simpleRawData2 = SimpleRawData(
            status = true,
            first_name = "Jane",
            last_name = "Doe",
            last_update = "01-01-2001 : 00:00"
        )
        val expected2 = SimpleRawDataValid(
            status = true,
            first_name = "Jane",
            last_name = "Doe"
        )

        Assert.assertEquals(expected, simpleRawData.validate())
        Assert.assertEquals(expected2, simpleRawData2.validate())
    }
}
