package com.app.data.models.enums

enum class ClaimStatus(val displayName: String) {

    CREATED("создана"),
    APPROVED("принята"),
    IN_PROGRESS("в работе"),
    TESTS_COMPLETED("тесты завершены"),
    DOCUMENTS_DELIVERED("документы доставлены")

}