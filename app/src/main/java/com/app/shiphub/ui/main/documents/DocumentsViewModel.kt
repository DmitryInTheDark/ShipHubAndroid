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
    fun getDocuments(claimId: Long) = withLoading {
        safeCall { claimsUseCase.getDocumentsByClaimId(claimId) }
            .handleResponse { emitState(DocumentsState.SetupDocumentByCurrentClaim(it.items)) }
    }

    init {
        withLoading {
            safeCall { claimsUseCase.getAllClaims() }
                .handleResponse { claims ->
                    val claims = claims.items
                    if (claims.isEmpty()){
                        emitState(DocumentsState.SetupEmptyState())
                    }else{
                        safeCall { claimsUseCase.getDocumentsByClaimId(claims.first().id) }
                            .handleResponse {  documents ->
                                emitState(DocumentsState.SetupDocumentsAndClaims(
                                    claims.map { it.id },
                                    documents.items
                                    )
                                )
                            }

                    }
                }
        }
    }

}