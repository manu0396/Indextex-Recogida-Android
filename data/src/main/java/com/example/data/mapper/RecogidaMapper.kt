package com.example.data.mapper

import com.example.data.db.AuthorizedEntity
import com.example.data.models.RecogidaResponse
import com.example.domain.model.ScanResult

class RecogidaMapper {

    fun mapToDomain(response: RecogidaResponse): ScanResult.Success {
        return ScanResult.Success(
            name = response.fullName ?: "Unknown",
            type = response.roleType ?: "Standard"
        )
    }

    fun mapToDomain(entity: AuthorizedEntity): ScanResult.Success {
        return ScanResult.Success(
            name = entity.name,
            type = entity.role
        )
    }

    // Used by validateQr (Single item validation)
    fun mapToEntity(domain: ScanResult.Success, originalQr: String): AuthorizedEntity {
        return AuthorizedEntity(
            qrCode = originalQr,
            name = domain.name,
            role = domain.type,
            lastUpdated = System.currentTimeMillis()
        )
    }

    // FIX: Add this overload for syncAuthorizedPersonnel (Bulk sync)
    // This resolves "Argument type mismatch: RecogidaResponse but ScanResult.Success expected"
    fun mapToEntity(response: RecogidaResponse): AuthorizedEntity {
        return AuthorizedEntity(
            qrCode = response.id, // Ensure RecogidaResponse has 'id' (the QR)
            name = response.fullName ?: "Unknown",
            role = response.roleType ?: "Standard",
            lastUpdated = System.currentTimeMillis()
        )
    }
}
