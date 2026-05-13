package com.app.data.storage.preferences

import android.content.Context
import com.app.data.models.domain.User
import com.app.data.models.enums.UserType
import androidx.core.content.edit

class UserPreferences(
    context: Context,
    private val legalInfoPreferences: LegalInfoPreferences,
    private val physicalInfoPreferences: PhysicalInfoPreferences
) {

    private val sharedPref = context.getSharedPreferences(USER_STORAGE, Context.MODE_PRIVATE)

    private var jwt: String
        get() = sharedPref.getString("jwt", null) ?: ""
        set(value) = sharedPref.edit { putString("jwt", value) }
    private var id: Int
        get() = sharedPref.getInt("id", -1)
        set(value) = sharedPref.edit { putInt("id", value) }
    private var username: String
        get() = sharedPref.getString("username", null) ?: ""
        set(value) = sharedPref.edit { putString("username", value) }
    private var email: String
        get() = sharedPref.getString("email", null) ?: ""
        set(value) = sharedPref.edit { putString("email", value) }
    private var type: String
        get() = sharedPref.getString("type", null) ?: ""
        set(value) = sharedPref.edit { putString("type", value) }

    fun getUser(): User {
        return User(
            id = id,
            username = username,
            email = email,
            type = if (type.isEmpty()) UserType.UNKNOWN else UserType.valueOf(type),
            legalInfo = legalInfoPreferences.getLegalInfo(),
            physicalInfo = physicalInfoPreferences.getPhysicalInfo()
        )
    }

    fun setUser(user: User): User{
        id = user.id
        username = user.username
        email = user.email
        type = user.type.name
        legalInfoPreferences.setLegalInfo(user.legalInfo)
        physicalInfoPreferences.setPhysicalInfo(user.physicalInfo)
        return getUser()
    }

    fun updateJwt(jwt: String){
        this.jwt = jwt
    }

    companion object{
        private const val USER_STORAGE = "user_storage"
    }

}