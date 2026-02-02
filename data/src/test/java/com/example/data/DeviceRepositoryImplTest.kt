package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.core_common.dispatchers.result.Result
import com.example.data.repository.DeviceRepositoryImpl
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class DeviceRepositoryImplTest {
    private val context: Context = mockk()
    private val sharedPrefs: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk()

    @Test
    fun `registerDevice should return success result`() = runTest {
        every { context.getSharedPreferences(any(), any()) } returns sharedPrefs
        every { sharedPrefs.getString(any(), any()) } returns "mock-id"
        every { sharedPrefs.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.apply() } just Runs

        val repository = DeviceRepositoryImpl(context)
        val result = repository.registerDevice("12345")

        assertTrue("Expected Result.Success, got $result", result is Result.Success)
    }
}
