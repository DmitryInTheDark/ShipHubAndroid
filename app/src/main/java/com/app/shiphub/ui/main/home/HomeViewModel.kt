package com.app.shiphub.ui.main.home

import com.app.base.BasePagingViewModel
import com.app.base.models.BaseListResponse
import com.app.base.safeCall
import com.app.data.models.domain.Claim
import com.app.data.models.response.NotificationDTO
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

    init {
        loadNotifications()
    }

    fun loadNotifications() = withLoading {
        safeCall { claimsUseCase.getNotifications() }.handleResponse {
            emitState(HomeUIState.ShowNotifications(it))
        }
    }

    fun getUser() = userUseCase.getUser()

    override suspend fun getPage(page: Int): BaseListResponse<Claim> {
        return claimsUseCase.getActiveClaims(page, 20)
    }

    override suspend fun onLoadError(e: Exception) {
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