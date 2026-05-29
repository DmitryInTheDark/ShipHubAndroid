package com.app.shiphub.ui.main.documents

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.app.base.BaseViewModel
import com.app.base.SimpleStates
import com.app.base.safeCall
import com.app.data.use_cases.ClaimsUseCase
import com.app.data.models.enums.DocumentType
import com.app.shiphub.ShipHubApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentsViewModel @Inject constructor(
    private val claimsUseCase: ClaimsUseCase
) : BaseViewModel<DocumentsState>(DocumentsState.InitScreen()) {

    private var currentClaimId: Long? = null

    // Храним pending документы до отправки
    private val pendingDocuments = mutableMapOf<DocumentType, MutableList<Uri>>()

    fun getDocuments(claimId: Long) = withLoading {
        currentClaimId = claimId
        safeCall { claimsUseCase.getDocumentsByClaimId(claimId) }
            .handleResponse {
                emitState(DocumentsState.SetupDocumentByCurrentClaim(it.items))
            }
    }

    init {
        getClaims()
    }

    fun getClaims() = withLoading {
        safeCall { claimsUseCase.getAllClaims() }
            .handleResponse { response ->
                val claims = response.items
                if (claims.isEmpty()) {
                    emitState(DocumentsState.SetupDocumentsAndClaims(emptyList(), emptyList()))
                } else {
                    currentClaimId = claims.first().id
                    getDocuments(claims.first().id)
                }
            }
    }

    fun getCurrentClaimId() = currentClaimId

    // === Новый функционал ===

    fun addDocument(uri: Uri, type: DocumentType) {
        val list = pendingDocuments.getOrPut(type) { mutableListOf() }
        list.add(uri)

        emitCurrentStateWithPending()
    }

    fun removePendingDocument(uri: Uri, type: DocumentType) {
        pendingDocuments[type]?.remove(uri)
        if (pendingDocuments[type]?.isEmpty() == true) {
            pendingDocuments.remove(type)
        }
        emitCurrentStateWithPending()
    }

    fun uploadPendingDocuments() = viewModelScope.launch {
        val claimId = currentClaimId ?: return@launch

        val documentParts = mutableListOf<okhttp3.MultipartBody.Part>()
        val documentTypes = mutableListOf<String>()

        pendingDocuments.forEach { (type, uris) ->
            uris.forEach { uri ->
                val part = com.app.base.FileUtils.createDocumentPart(ShipHubApplication.instance.applicationContext, uri, "documents")
                    ?: return@forEach
                documentParts.add(part)
                documentTypes.add(type.name)
            }
        }

        if (documentParts.isEmpty()) return@launch

        withLoading {
            safeCall {
                claimsUseCase.attachDocuments(claimId, documentParts, documentTypes)
            }.handleResponse { resultMap ->
                // resultMap: Map<Filename, "Успешно" или ошибка>

                val successCount = resultMap.count { it.value == "Успешно" }
                val hasErrors = resultMap.any { it.value != "Успешно" }

                if (hasErrors) {
                    emitSimpleState(SimpleStates.Error("Загружено $successCount из ${resultMap.size}. Есть ошибки."))
                }

                // Очищаем pending после попытки загрузки
                pendingDocuments.clear()

                // Обновляем список документов
                getDocuments(claimId)
            }
        }
    }

    private fun emitCurrentStateWithPending() {
        viewModelScope.launch {
            when (val currentState = _state.value) {
                is DocumentsState.SetupDocumentByCurrentClaim -> {
                    emitState(currentState.copy(pendingDocuments = pendingDocuments.toMap()))
                }
                is DocumentsState.SetupDocumentsAndClaims -> {
                    emitState(currentState.copy(pendingDocuments = pendingDocuments.toMap()))
                }
                else -> {
                    // fallback
                }
            }
        }
    }
}