package com.app.data.models.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LegalInfo(
    val organizationName: String,
    val inn: String,
    val kpp: String,
    val address: String,
    val phone: String
): Parcelable
