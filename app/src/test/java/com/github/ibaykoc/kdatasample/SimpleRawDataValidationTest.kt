package com.github.ibaykoc.kdatasample

import com.github.ibaykoc.kdataannotation.KData
import org.junit.Assert
import org.junit.Test

class SimpleRawDataValidationTest {

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

    @Test
    fun `Test simple raw data valid`() {
        val simpleRawData = SimpleRawData(
            status = null,
            first_name = "Jane",
            last_name = "Doe",
            last_update = "01-01-2001 : 00:00"
        )
        println(simpleRawData.validate())
        val expected = SimpleRawDataValidated(
            first_nameValidated = "Jane",
            last_nameValidated = "Doe"
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
        val expected = SimpleRawDataValidated(
            statusValidated = null,
            first_nameValidated = "Jane",
            last_nameValidated = "Doe"
        )
        val simpleRawData2 = SimpleRawData(
            status = true,
            first_name = "Jane",
            last_name = "Doe",
            last_update = "01-01-2001 : 00:00"
        )
        val expected2 = SimpleRawDataValidated(
            statusValidated = true,
            first_nameValidated = "Jane",
            last_nameValidated = "Doe"
        )

        Assert.assertEquals(expected, simpleRawData.validate())
        Assert.assertEquals(expected2, simpleRawData2.validate())
    }
}
