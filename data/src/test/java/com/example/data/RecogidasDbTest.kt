package com.example.data

import com.example.data.db.RecogidaDatabase
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver // Added missing import
import junit.framework.TestCase.assertEquals
import org.junit.Test

class RecogidasDbTest {
    @Test
    fun `insert and select authorized user`() {
        // Resolves: Unresolved reference 'JdbcSqliteDriver'
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        RecogidaDatabase.Schema.create(driver)
        val queries = RecogidaDatabase(driver).recogidaQueries

        // Matches the 4-parameter signature from the .sq file
        queries.insertUser("789", "John Doe", "ADMIN", 1769079870924L)

        // Resolves: Unresolved reference 'selectAll'
        val result = queries.selectAll().executeAsList()

        assertEquals(1, result.size)
        assertEquals("John Doe", result[0].name)
    }
}
