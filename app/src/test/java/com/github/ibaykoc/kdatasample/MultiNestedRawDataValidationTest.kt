package com.github.ibaykoc.kdatasample

import com.github.ibaykoc.kdatasample.data.MultiNestedRawData
import com.github.ibaykoc.kdatasample.data.MultiNestedRawDataValid
import com.github.ibaykoc.kdatasample.data.validate
import org.junit.Assert
import org.junit.Test

class MultiNestedRawDataValidationTest {

    @Test
    fun `Test multi nested data valid`() {
        val multiNestedRawData = MultiNestedRawData(
            account = MultiNestedRawData.Account(
                status = null,
                first_name = "Jane",
                last_name = "Doe",
                dob = MultiNestedRawData.DOB(1, 1, 1999)
            ),
            last_update = "01-01-2001 : 00:00"
        )
        println(multiNestedRawData.validate())
        val expected = MultiNestedRawDataValid(
            account = MultiNestedRawDataValid.Account(
                status = null,
                first_name = "Jane",
                last_name = "Doe",
                dob = MultiNestedRawDataValid.Account.DOB(
                    1, 1, 1999
                )
            )
        )

        Assert.assertEquals(expected, multiNestedRawData.validate())
    }

    @Test
    fun `Test multi nested raw data not valid`() {
        val multiNestedRawData = MultiNestedRawData(
            account = MultiNestedRawData.Account(
                status = true,
                first_name = "Jane",
                last_name = "Doe",
                dob = MultiNestedRawData.DOB(null, 1, 1999)
            ),
            last_update = "01-01-2001 : 00:00"
        )
        val multiNestedRawData2 = MultiNestedRawData(
            account = MultiNestedRawData.Account(
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