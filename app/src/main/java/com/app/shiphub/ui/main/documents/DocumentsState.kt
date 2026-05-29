package com.app.shiphub.ui.main.documents

import android.net.Uri
import com.app.base.BaseState
import com.app.data.models.domain.Document
import com.app.data.models.enums.DocumentType

sealed class DocumentsState : BaseState() {
    class InitScreen : DocumentsState()

    data class SetupDocumentsAndClaims(
        val claimsIds: List<Long>,
        val currentDocuments: List<Document>,
        val pendingDocuments: Map<DocumentType, List<Uri>> = emptyMap()
    ) : DocumentsState()

    data class SetupDocumentByCurrentClaim(
        val documents: List<Document>,
        val pendingDocuments: Map<DocumentType, List<Uri>> = emptyMap()
    ) : DocumentsState()
}