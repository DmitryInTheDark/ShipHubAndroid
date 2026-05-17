package com.app.shiphub.ui.main.claims

import com.app.base.BasePagingViewModel
import com.app.base.models.BaseListResponse
import com.app.data.models.domain.Claim
import com.app.data.models.enums.ClaimStatus
import com.app.data.use_cases.ClaimsUseCase
import com.app.shiphub.ui.main.claims.adapter.ClaimsHolderModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClaimsViewModel @Inject constructor(
    private val claimsUseCase: ClaimsUseCase,
): BasePagingViewModel<Claim, ClaimsUIState, ClaimsHolderModel>(ClaimsUIState.InitScreen()) {

    override suspend fun getPage(page: Int): BaseListResponse<Claim> =
        claimsUseCase.getClaims(page)

    override fun map(domain: Claim): ClaimsHolderModel = with(domain){
        return ClaimsHolderModel(id)
    }

    fun filterClaimsByStatus(status: ClaimStatus) {

    }
}