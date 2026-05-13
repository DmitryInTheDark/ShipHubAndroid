package com.app.shiphub.util

object BaseValidator {

    fun validateEmail(email: String?): List<String> {
        val errors = mutableListOf<String>()
        val isBlank = email.isNullOrBlank()
        val emailLength = email?.length ?: 0

        if (isBlank) {
            errors.add("Email не может быть пустым")
        }
        if (emailLength < 3) {
            errors.add("Email должен быть не менее трёх символов в длину")
        }
        if (emailLength > 254) {
            errors.add("Слишком длинный Email")
        }
        val emailRegex = Regex(
            "^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?" +
                    "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"
        )
        if (!isBlank && !emailRegex.matches(email)){
            errors.add("Некорректный email")
        }
        return errors
    }

    fun validatePassword(password: String?): List<String> {

        val errors = mutableListOf<String>()
        val isBlank = password.isNullOrBlank()
        val passwordLength = password?.length ?: 0

        if (isBlank) {
            errors.add("пароль не может быть пустым")
        }
        if (passwordLength < 5) {
            errors.add("пароль должен быть не менее пяти символов в длину")
        }
        if (passwordLength > 64) {
            errors.add("Слишком длинный пароль")
        }

        return errors
    }

}