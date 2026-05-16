package com.app.data.use_cases

import com.app.data.UserRepository
import com.app.data.models.domain.User
import javax.inject.Inject


class UserUseCase @Inject constructor(
    private val userRepository: UserRepository
){

    fun getUser(): User = userRepository.getUser()

}