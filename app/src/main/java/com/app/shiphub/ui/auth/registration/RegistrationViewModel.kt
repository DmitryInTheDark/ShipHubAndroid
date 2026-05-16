package com.app.shiphub.ui.auth.registration

import com.app.base.BaseViewModel
import com.app.base.safeCall
import com.app.data.models.domain.BaseUserInfo
import com.app.data.models.domain.LegalInfo
import com.app.data.models.domain.PhysicalInfo
import com.app.data.use_cases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : BaseViewModel<RegistrationUIState>(RegistrationUIState.InitScreen()) {

    fun registerLegal(baseInfo: BaseUserInfo, legalInfo: LegalInfo) {
        withLoading {
            val response = safeCall { authUseCase.registrationLegal(baseInfo, legalInfo) }
            response.handleResponse {
                _state.value = RegistrationUIState.SuccessRegistration(baseInfo.email)
            }
        }
    }

    fun registerPhysical(baseInfo: BaseUserInfo, physicalInfo: PhysicalInfo) {
        withLoading {
            val response = safeCall { authUseCase.registrationPhysical(baseInfo, physicalInfo) }
            response.handleResponse{
                _state.value = RegistrationUIState.SuccessRegistration(baseInfo.email)
            }
        }
    }

}
