package com.app.shiphub.ui.main.home

import com.app.base.BasePagingViewModel
import com.app.base.models.BaseListResponse
import com.app.data.models.domain.Claim
import com.app.data.use_cases.ClaimsUseCase
import com.app.data.use_cases.UserUseCase
import com.app.shiphub.ui.main.home.adapters.HomeClaimHolderModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val claimsUseCase: ClaimsUseCase,
    private val userUseCase: UserUseCase
) : BasePagingViewModel<Claim, HomeUIState, HomeClaimHolderModel>(
    HomeUIState.InitUserInfo(userUseCase.getUser(), emptyList())
){

    override suspend fun getPage(page: Int): BaseListResponse<Claim> {
        return claimsUseCase.getActiveClaims(page, 20)
    }

    override fun loadPage(page: Int) {
        super.loadPage(page)
    }

    override fun map(domain: Claim): HomeClaimHolderModel {
        return HomeClaimHolderModel(
            id = domain.id,
            testType = domain.testType.displayName,
            status = domain.status.displayName,
            lastUpdate = domain.lastUpdate ?: ""
        )
    }


}