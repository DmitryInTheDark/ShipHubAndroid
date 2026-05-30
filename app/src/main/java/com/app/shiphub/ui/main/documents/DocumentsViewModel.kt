package com.app.shiphub.ui.main.documents

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.app.base.BaseViewModel
import com.app.base.SimpleStates
import com.app.base.safeCall
import com.app.data.use_cases.ClaimsUseCase
import com.app.data.models.enums.DocumentType
import com.app.data.use_cases.UserUseCase
import com.app.shiphub.ShipHubApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.collections.sorted

@HiltViewModel
class DocumentsViewModel @Inject constructor(
    private val claimsUseCase: ClaimsUseCase,
    private val userUseCase: UserUseCase
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

    fun isManager(): Boolean{
        return userUseCase.isManager()
    }

    fun getClaims() = withLoading {
        safeCall { claimsUseCase.getAllClaims() }
            .handleResponse { response ->
                val claims = response.items
                if (claims.isEmpty()) {
                    emitState(DocumentsState.SetupDocumentsAndClaims(emptyList(), emptyList()))
                } else {
                    if (currentClaimId == null || claims.none { it.id == currentClaimId }){
                        currentClaimId = claims.first().id
                    }
                    emitState(DocumentsState.SetupClaims(claims.map { it.id }.sorted()))
                    getDocuments(currentClaimId ?: claims.first().id)
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

        val documentParts = mutableListOf<MultipartBody.Part>()
        val documentTypes = mutableListOf<String>()

        pendingDocuments.forEach { (type, uris) ->
            uris.forEachIndexed { index, uri ->
                val file = uriToFile(ShipHubApplication.instance.applicationContext, uri, "document${index + 1}")
                val mimeType = com.app.base.FileUtils.getMimeType(ShipHubApplication.instance.applicationContext, uri)
                val requestFile = file.asRequestBody(mimeType?.toMediaTypeOrNull())
//                val part = com.app.base.FileUtils.createDocumentPart(ShipHubApplication.instance.applicationContext, uri, "documents")
//                    ?: return@forEach
                documentParts.add(MultipartBody.Part.createFormData("documents", file.name, requestFile))
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
                    emitSimpleState(SimpleStates.Error(
                        "Загружено $successCount из ${resultMap.size}. \n ${resultMap.entries.joinToString("\n") { "${it.key}: ${it.value}" }}")
                    )
                }

                // Очищаем pending после попытки загрузки
                pendingDocuments.clear()

                // Обновляем список документов
                getDocuments(claimId)
            }
        }
    }

    private fun uriToFile(context: android.content.Context, uri: Uri, fileNamePrefix: String): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = com.app.base.FileUtils.getFileName(context, uri) ?: fileNamePrefix
        val file = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
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