package com.app.data.storage.preferences

import android.content.Context
import androidx.core.content.edit
import com.app.data.models.domain.PhysicalInfo

class PhysicalInfoPreferences(
    context: Context
) {

    private val sharedPref = context.getSharedPreferences(PHYSICAL_INFO_STORAGE, Context.MODE_PRIVATE)

    private var address: String
        get() = sharedPref.getString("address", "") ?: ""
        set(value) = sharedPref.edit { putString("address", value) }

    fun getPhysicalInfo(): PhysicalInfo {
        return PhysicalInfo(
            address = address
        )
    }

    fun setPhysicalInfo(physicalInfo: PhysicalInfo?) {
        address = physicalInfo?.address ?: ""
    }


    companion object{
        private const val PHYSICAL_INFO_STORAGE = "physical_info_storage"
    }
}
