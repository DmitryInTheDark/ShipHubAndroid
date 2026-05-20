package com.app.shiphub.ui.main.documents

import com.app.base.BaseState
import com.app.data.models.domain.Document

sealed class DocumentsState : BaseState(){
    class InitScreen(): DocumentsState()
    class SetupDocumentsAndClaims(val claimsIds: List<Long>, val currentDocuments: List<Document>): DocumentsState()
    class SetupDocumentByCurrentClaim(val documents: List<Document>): DocumentsState()
    class SetupEmptyState(): DocumentsState()
}