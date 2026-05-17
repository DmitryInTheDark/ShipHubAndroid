package com.app.shiphub.ui.main.claims.adapter

import com.app.base.BaseHolderModel
import com.app.data.models.enums.ClaimStatus

sealed class ClaimHolders: BaseHolderModel {

    data class ClaimsHolderModel(
        val id: Long,
        val testType: String,
        val equipment: String,
        val status: String,
        val lastUpdate: String
    ): ClaimHolders()

    data class ClaimsHeaderHolderModel(
        val checkedStatus: ClaimStatus?,
        val scrollX: Int?
    ): ClaimHolders()
}