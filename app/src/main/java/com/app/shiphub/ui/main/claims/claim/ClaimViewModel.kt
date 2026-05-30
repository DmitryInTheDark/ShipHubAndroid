package com.app.shiphub.ui.main.claims.claim

import com.app.base.BaseViewModel
import com.app.base.SimpleStates
import com.app.base.handleError
import com.app.base.safeCall
import com.app.data.models.enums.ClaimStatus
import com.app.data.use_cases.ClaimsUseCase
import com.app.data.use_cases.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClaimViewModel @Inject constructor(
    private val claimsUseCase: ClaimsUseCase,
    private val userUseCase: UserUseCase
): BaseViewModel<ClaimUIState>(ClaimUIState.Init()) {

    fun getUser() = userUseCase.getUser()
    fun getClaim(claimId: Long) = withLoading{
        val result = safeCall { claimsUseCase.getClaimById(claimId) }
        result.onSuccess { emitState(ClaimUIState.InitScreen(it, emptyList())) }
            .onFailure { emitSimpleState(SimpleStates.Error(it.handleError())) }
    }

    fun updateStatus(claimId: Long, status: ClaimStatus) = withLoading {
        val result = safeCall { claimsUseCase.updateClaimStatus(claimId, status) }
        result.onSuccess {
            getClaim(claimId)
        }.onFailure {
            emitSimpleState(SimpleStates.Error(it.handleError()))
        }
    }
}