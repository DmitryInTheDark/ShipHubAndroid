package com.app.domain.use_case

import com.app.domain.models.User

class AuthUseCase @Inject() {

    fun login(
        email: String,
        password: String
    ): User{
        return
    }

}