package com.example.data_core

import android.content.Context
import com.example.data_core.hardware.PdaHardwareWrapperImpl
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import com.example.data.api.RecogidasApi

class PdaHardwareWrapperImplTest {
    private val context: Context = mockk(relaxed = true)
    private val wrapper = PdaHardwareWrapperImpl(context)
    private val api: RecogidasApi = mockk()

    @Test
    fun `when triggerVibration is called, vibrator service is accessed`() {
        wrapper.triggerVibration(500L)
        verify { context.getSystemService(Context.VIBRATOR_SERVICE) }
    }

    @Test
    fun `interceptor should add auth header to requests`() {
        val server = MockWebServer()
        server.enqueue(MockResponse().setBody("{}"))
        // coEvery { api.getSomething() } returns ...
        val request = server.takeRequest()
        assertEquals("Bearer valid_token", request.getHeader("Authorization"))
        server.shutdown()
    }
}
