package com.github.ibaykoc.kdatasample

import com.github.ibaykoc.kdataannotation.KData
import org.junit.Assert
import org.junit.Test

class MultiNestedRawDataValidationTest {

    @KData
    data class MultiNestedRawData(
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

    @Test
    fun `Test multi nested data valid`() {
        val multiNestedRawData = MultiNestedRawData(
            account = Account(
                status = null,
                first_name = "Jane",
                last_name = "Doe",
                dob = DOB(1, 1, 1999)
            ),
            last_update = "01-01-2001 : 00:00"
        )
        println(multiNestedRawData.validate())
        val expected = MultiNestedRawDataValidated(
            first_nameValidated = "Jane",
            last_nameValidated = "Doe",
            dayValidated = 1,
            monthValidated = 1,
            yearValidated = 1999
        )

        Assert.assertEquals(expected, multiNestedRawData.validate())
    }

    @Test
    fun `Test multi nested raw data not valid`() {
        val multiNestedRawData = MultiNestedRawData(
            account = Account(
                status = true,
                first_name = "Jane",
                last_name = "Doe",
                dob = DOB(null, 1, 1999)
            ),
            last_update = "01-01-2001 : 00:00"
        )
        val multiNestedRawData2 = MultiNestedRawData(
            account = Account(
                status = true,
                first_name = "Jane",
                last_name = "Doe",
                dob = null
            ),
            last_update = "01-01-2001 : 00:00"
        )
        val expected = null

        Assert.assertEquals(expected, multiNestedRawData.validate())
        Assert.assertEquals(expected, multiNestedRawData2.validate())
    }
}