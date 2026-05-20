package com.app.data.models.domain

import com.app.data.models.enums.DocumentType

data class Document(
    val id: Long,
    val name: String,
    val type: DocumentType,
    val contentType: String,
    val dateCreate: String
)