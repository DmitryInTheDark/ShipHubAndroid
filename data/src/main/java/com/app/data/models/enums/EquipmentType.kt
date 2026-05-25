package com.app.data.models.enums

import kotlinx.serialization.Serializable

@Serializable
enum class EquipmentType(val displayName: String) {

    ELECTRO_TOOLS("Электрообуродование"),
    MACHINES("Станки"),
    INSTRUMENTATION_TOOLS("КИПиА"),
    MEDICAL("Медицинское оборудование"),
    OTHER("Другое");

}