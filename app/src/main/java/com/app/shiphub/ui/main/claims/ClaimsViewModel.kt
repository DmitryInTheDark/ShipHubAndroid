package com.app.shiphub.ui.main.claims

import androidx.lifecycle.SavedStateHandle
import com.app.base.BasePagingViewModel
import com.app.base.models.BaseListResponse
import com.app.data.models.domain.Claim
import com.app.data.models.enums.ClaimStatus
import com.app.data.use_cases.ClaimsUseCase
import com.app.shiphub.ui.main.claims.adapter.ClaimHolders
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClaimsViewModel @Inject constructor(
    private val claimsUseCase: ClaimsUseCase,
    savedStateHandle: SavedStateHandle
): BasePagingViewModel<Claim, ClaimsUIState, ClaimHolders>(ClaimsUIState.InitScreen()) {

    private var currentStatus: ClaimStatus? = savedStateHandle.get<String>("status")?.let { statusName ->
            ClaimStatus.entries.find { it.name == statusName || it.displayName == statusName }
        }

    override suspend fun getPage(page: Int): BaseListResponse<Claim> {
        return if (currentStatus == null){
            claimsUseCase.getClaims(page)
        }else{
            claimsUseCase.getClaimsByStatus(page, status = currentStatus!!)
        }
    }

    override fun map(domain: Claim): ClaimHolders.ClaimsHolderModel = with(domain){
        return ClaimHolders.ClaimsHolderModel(id, testType.displayName, equipment.name, status.displayName, lastUpdate ?: "")
    }

    fun filterClaimsByStatus(status: ClaimStatus?) {
        currentStatus = status
        loadFirstPage()
    }

    fun getCurrentStatus(): ClaimStatus? = currentStatus

}
