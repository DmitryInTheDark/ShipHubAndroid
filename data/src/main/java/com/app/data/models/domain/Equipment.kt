package com.app.data.models.domain

import android.os.Parcelable
import com.app.data.models.enums.EquipmentType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Equipment(
    val id: Long,
    val equipmentType: EquipmentType,
    val name: String,
    val manufacturer: String,
    val serialNumber: String,
    val count: Int,
    val imageIds: List<Long> = emptyList(),
    val isCustomType: Boolean,
    val customType: String?
): Parcelable
