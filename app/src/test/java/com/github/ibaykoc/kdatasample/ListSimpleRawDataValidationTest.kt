package com.github.ibaykoc.kdatasample

import com.github.ibaykoc.kdatasample.data.ListSimpleRawData
import com.github.ibaykoc.kdatasample.data.ListSimpleRawDataValidated
import com.github.ibaykoc.kdatasample.data.validate
import org.junit.Assert
import org.junit.Test

class ListSimpleRawDataValidationTest {

    @Test
    fun `Test simple raw data valid`() {
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

        val expected = ListSimpleRawDataValidated(
            usernameValidated = "Jane",
            commentsValidated = listOf("First Comment"),
            repositoriesValidated = listOf(
                ListSimpleRawDataValidated.RepositoryValidated(
                    nameValidated = "JaneRepos",
                    starValidated = 5
                )
            )
        )
        Assert.assertEquals(expected, listSimpleRawData.validate())
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