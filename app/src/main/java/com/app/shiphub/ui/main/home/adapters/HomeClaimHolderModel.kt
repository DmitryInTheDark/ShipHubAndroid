package com.app.shiphub.ui.main.home.adapters

import com.app.base.BaseHolderModel

data class HomeClaimHolderModel(
    val id: Long,
    val testType: String,
    val status: String,
    val lastUpdate: String
): BaseHolderModel
