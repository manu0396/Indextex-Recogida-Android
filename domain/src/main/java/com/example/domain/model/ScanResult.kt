package com.example.domain.model

sealed class ScanResult {
    data class Success(val name: String, val type: String) : ScanResult()
    data class Error(val message: String) : ScanResult()
    data object Loading: ScanResult()
    data object Idle : ScanResult()
    data class FormatError(
        val read: String,
        val expected: String = "INDITEX-XXXXXX"
    ) : ScanResult()
}
