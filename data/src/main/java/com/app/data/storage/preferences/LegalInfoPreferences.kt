package com.app.data.storage.preferences

import android.content.Context
import androidx.core.content.edit
import com.app.data.models.domain.LegalInfo

class LegalInfoPreferences(
    context: Context
) {

    private val sharedPref = context.getSharedPreferences(LEGAL_INFO_STORAGE, Context.MODE_PRIVATE)

    private var organizationName: String
        get() = sharedPref.getString("organizationName", "") ?: ""
        set(value) = sharedPref.edit { putString("organizationName", value) }
    private var inn: String
        get() = sharedPref.getString("inn", "") ?: ""
        set(value) = sharedPref.edit { putString("inn", value) }
    private var kpp: String
        get() = sharedPref.getString("kpp", "") ?: ""
        set(value) = sharedPref.edit { putString("kpp", value) }
    private var address: String
        get() = sharedPref.getString("address", "") ?: ""
        set(value) = sharedPref.edit { putString("address", value) }
    private var phone: String
        get() = sharedPref.getString("phone", "") ?: ""
        set(value) = sharedPref.edit { putString("phone", value) }

    fun getLegalInfo(): LegalInfo{
        return LegalInfo(
            organizationName = organizationName,
            inn = inn,
            kpp = kpp,
            address = address,
            phone = phone
        )
    }

    fun setLegalInfo(legalInfo: LegalInfo?) {
        organizationName = legalInfo?.organizationName ?: ""
        inn = legalInfo?.inn ?: ""
        kpp = legalInfo?.kpp ?: ""
        address = legalInfo?.address ?: ""
        phone = legalInfo?.phone ?: ""
    }

    companion object{
        private const val LEGAL_INFO_STORAGE = "legal_info_storage"
    }

}