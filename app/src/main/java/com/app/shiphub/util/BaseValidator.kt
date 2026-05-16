package com.app.shiphub.util

object BaseValidator {

    val codeRegex = Regex("^[A-Z0-9]+$")
    val digitRegex = Regex("^\\d$")

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

    fun validateInn(inn: String): List<String>{
        val errors = mutableListOf<String>()
        val innRegex = Regex("^\\d+$")

        if (!innRegex.matches(inn)) errors.add("ИНН должен содержать только цифры")
        if (inn.length != 10) errors.add("ИНН должен содержать 10 цифр")
        return errors
    }

    fun validateKpp(kpp: String): List<String>{
        val errors = mutableListOf<String>()
        val kppRegex = Regex("^\\d+$")

        if (!kppRegex.matches(kpp)) errors.add("КПП должен содержать только цифры")
        if (kpp.length != 9) errors.add("КПП должен содержать 9 цифр")
        return errors
    }

    fun validateOrgName(orgName: String): List<String>{
        val errors = mutableListOf<String>()
        if (orgName.isBlank()) errors.add("Название организации не может быть пустым")
        if (orgName.length < 3) errors.add("Название организации должно быть не менее трёх символов в длину")
        return errors
    }

    fun validateAddress(address: String): List<String>{
        val errors = mutableListOf<String>()
        if (address.isBlank()) errors.add("Название организации не может быть пустым")
        if (address.length < 3) errors.add("Название организации должно быть не менее трёх символов в длину")
        return errors
    }

    fun validateFio(fio: String): List<String>{
        val errors = mutableListOf<String>()
        if (fio.isBlank()) errors.add("ФИО не может быть пустым")
        return errors
    }

    fun validatePhone(phone: String): List<String>{
        val errors = mutableListOf<String>()
        if (phone.isBlank()) errors.add("телефон не может быть пустым")
        val regex = Regex("^\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}$")
        if (!regex.matches(phone)) errors.add("Телефон содержит недопустимые символы")
        return errors
    }

    fun validateEmailCode(code: String): List<String>{
        val errors = mutableListOf<String>()
        if (code.length < 5) errors.add("Код должен состоять из 5 символов")
        if (code.toCharArray().filter { !digitRegex.matches(it.toString()) }.any{ !it.isUpperCase() }) errors.add("Код должен быть только из символов заглавного регистра")
        if (!codeRegex.matches(code)) errors.add("Код содержит недопустимые символы")
        return errors
    }

}