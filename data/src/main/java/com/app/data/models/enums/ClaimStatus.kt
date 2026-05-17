package com.app.data.models.enums

enum class ClaimStatus(val displayName: String) {

    CREATED("Создана"),
    APPROVED("Принята"),
    IN_PROGRESS("В работе"),
    TESTS_COMPLETED("Тесты завершены"),
    DOCUMENTS_DELIVERED("Документы доставлены")

}