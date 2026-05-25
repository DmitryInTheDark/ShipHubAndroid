package com.app.data

import com.app.data.models.domain.User
import com.app.data.storage.preferences.UserPreferences
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userPreferences: UserPreferences
) {

    fun saveUser(user: User): User{
        userPreferences.setUser(user)
        return userPreferences.getUser()
    }

    fun updateJWT(jwt: String){
        userPreferences.updateJwt(jwt)
    }

    fun isUserAuthorized(): Boolean{
        return userPreferences.jwt().isNotBlank()
    }

    fun getJwt(): String{
        return userPreferences.jwt()
    }

    fun getUser(): User = userPreferences.getUser()

    fun clearData(){
        userPreferences.clearData()
    }
}