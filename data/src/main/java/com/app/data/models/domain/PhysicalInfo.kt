package com.app.data.models.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhysicalInfo(
    val address: String
): Parcelable