package com.app.data.models.enums

enum class TestType(val displayName: String) {

    SECURITY("Испытания на безопасность"),
    ELECTROMAGNETIC_COMPATIBILITY("Электромагнитная совместимость"),
    CLIMATE_TEST("Климатические испытания"),
    MECHANIC("Механические испытания"),
    MATERIAL_ANALYZE("Анализ материалов"),
    CERTIFICATION("Сертификация"),
    OTHER("Другое");

}