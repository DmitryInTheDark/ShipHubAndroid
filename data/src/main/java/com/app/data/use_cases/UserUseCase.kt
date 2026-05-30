package com.app.data.use_cases

import com.app.data.UserRepository
import com.app.data.api.UserApi
import com.app.data.models.domain.User
import com.app.data.models.enums.UserType
import com.app.data.models.request.UpdateUserRequest
import javax.inject.Inject


class UserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userApi: UserApi
){

    fun getUser(): User = userRepository.getUser()

    suspend fun getUserFromServer(): User {
        val user = userApi.getUser()
        userRepository.saveUser(user)
        return getUser()
    }

    suspend fun updateUser(id: Long, request: UpdateUserRequest): User {
        val updatedUser = userApi.updateUser(id, request)
        userRepository.saveUser(updatedUser)
        return updatedUser
    }

    fun exit(){
        userRepository.clearData()
    }

    fun isManager() = userRepository.getUser().type == UserType.MANAGER
}