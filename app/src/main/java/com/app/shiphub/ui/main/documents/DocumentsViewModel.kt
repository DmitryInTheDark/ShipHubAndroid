package com.app.shiphub.ui.main.documents

import com.app.base.BaseViewModel
import com.app.base.safeCall
import com.app.data.use_cases.ClaimsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DocumentsViewModel @Inject constructor(
    private val claimsUseCase: ClaimsUseCase
): BaseViewModel<DocumentsState>(DocumentsState.InitScreen()) {

    private var currentClaimId: Long? = null
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
                    safeCall { claimsUseCase.getDocumentsByClaimId(claims.first().id) }
                        .handleResponse { documents ->
                            emitState(
                                DocumentsState.SetupDocumentsAndClaims(
                                    claims.map { it.id },
                                    documents.items
                                )
                            )
                        }
                }
            }
    }

    fun getCurrentClaimId() = currentClaimId
}