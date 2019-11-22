package com.github.ibaykoc.kdatasample

import com.github.ibaykoc.kdatasample.data.ListSimpleRawData
import com.github.ibaykoc.kdatasample.data.ListSimpleRawDataValid
import com.github.ibaykoc.kdatasample.data.validate
import org.junit.Assert
import org.junit.Test

class ListSimpleRawDataValidationTest {

    @Test
    fun `Test list simple raw data valid`() {
        val listSimpleRawData = ListSimpleRawData(
            username = "Jane",
            comments = listOf("First Comment"),
            repositories = listOf(
                ListSimpleRawData.Repository(
                    "JaneRepos",
                    star = 5
                )
            )
        )

        val expected = ListSimpleRawDataValid(
            username = "Jane",
            comments = listOf("First Comment"),
            repositories = listOf(
                ListSimpleRawDataValid.Repository(
                    name = "JaneRepos",
                    star = 5
                )
            )
        )

        val listSimpleRawData2 = ListSimpleRawData(
            username = "Jane",
            comments = null,
            repositories = listOf(
                ListSimpleRawData.Repository(
                    "JaneRepos",
                    star = 5
                )
            )
        )

        val expected2 = ListSimpleRawDataValid(
            username = "Jane",
            comments = null,
            repositories = listOf(
                ListSimpleRawDataValid.Repository(
                    name = "JaneRepos",
                    star = 5
                )
            )
        )
        Assert.assertEquals(expected, listSimpleRawData.validate())
        Assert.assertEquals(expected2, listSimpleRawData2.validate())
    }

    @Test
    fun `Test simple raw data not valid`() {
        val listSimpleRawData1 = ListSimpleRawData(
            username = null,
            comments = listOf("First Comment"),
            repositories = listOf(
                ListSimpleRawData.Repository(
                    "JaneRepos",
                    star = 5
                )
            )
        )
        val listSimpleRawData2 = ListSimpleRawData(
            username = "Jane",
            comments = null,
            repositories = null
        )
        val listSimpleRawData3 = ListSimpleRawData(
            username = "Jane",
            comments = listOf("First Comment"),
            repositories = null
        )

        val expected = null
        Assert.assertEquals(expected, listSimpleRawData1.validate())
        Assert.assertEquals(expected, listSimpleRawData2.validate())
        Assert.assertEquals(expected, listSimpleRawData3.validate())
    }
}