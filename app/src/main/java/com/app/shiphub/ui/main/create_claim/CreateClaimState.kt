package com.app.shiphub.ui.main.create_claim

import com.app.base.BaseState
import android.net.Uri

sealed class CreateClaimState : BaseState() {
    data object Init : CreateClaimState()
    data class Content(
        val organizationName: String = "",
        val clientName: String = "",
        val contactPhone: String = "",
        val email: String = "",
        val testTypeIndex: Int? = null,
        val isCustomType: Boolean = false,
        val customTestName: String = "",
        val additionalInfo: String = "",
        val equipmentName: String = "",
        val equipmentTypeIndex: Int? = null,
        val manufacturer: String = "",
        val serialNumber: String = "",
        val count: String = "1",
        val photos: List<Uri> = emptyList(),
        val documents: List<Uri> = emptyList()
    ) : CreateClaimState()
    data object Success : CreateClaimState()
    data class Error(val message: String) : CreateClaimState()
}
