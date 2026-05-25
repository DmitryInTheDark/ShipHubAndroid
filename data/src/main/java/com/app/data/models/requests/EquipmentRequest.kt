package com.app.data.models.requests

import com.app.data.models.enums.EquipmentType
import kotlinx.serialization.Serializable

@Serializable
data class EquipmentRequest(
    val equipmentType: EquipmentType?,
    val name: String,
    val manufacturer: String,
    val serialNumber: String,
    val count: Int,
    val isCustomType: Boolean = false,
    val customType: String? = null
)
