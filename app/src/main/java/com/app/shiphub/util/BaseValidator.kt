package com.app.shiphub.util

object BaseValidator {

    fun validateEmail(email: String?, action: (List<String>) -> Unit) {
//        val errors = mutableListOf<String>()
//
//        if (email.isNullOrBlank()) errors.add("Email не может быть пустым")
//
//        if (email.length < 3 || email.length > 254) {
//            errors.add("Email должен быть больше трёх символов в длину")
//        }
//
//        val emailRegex = Regex(
//            "^[A-Za-z0-9.!#\$%&'*+/=?^_`{|}~-]+@" +
//                    "[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)+$"
//        )
//
//        return emailRegex.matches(email)
    }

}