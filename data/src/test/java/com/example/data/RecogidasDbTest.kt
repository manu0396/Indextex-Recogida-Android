package com.example.data

import com.example.data.db.RecogidaDatabase
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver // Added missing import
import com.example.data.db.AuthorizedEntity
import junit.framework.TestCase.assertEquals
import org.junit.Test

class RecogidasDbTest {
    @Test
    fun `insert and select authorized user`() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        RecogidaDatabase.Schema.create(driver)
        val queries = RecogidaDatabase(driver).recogidaQueries

        queries.insertUser(AuthorizedEntity(
            qrCode = "USR-99",
            name = "John Doe",
            role = "ADMIN",
            lastUpdated = 1234567890
        ))
        val result = queries.selectAll().executeAsList()

        assertEquals(1, result.size)
        assertEquals("John Doe", result[0].name)
    }
}
