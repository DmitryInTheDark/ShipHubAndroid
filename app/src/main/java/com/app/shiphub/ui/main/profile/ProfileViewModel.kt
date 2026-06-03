package com.app.shiphub.ui.main.profile

import com.app.base.BaseViewModel
import com.app.base.safeCall
import com.app.data.models.domain.LegalInfo
import com.app.data.models.domain.PhysicalInfo
import com.app.data.models.request.UpdateUserRequest
import com.app.data.use_cases.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): BaseViewModel<ProfileState>(ProfileState.Init) {

    init {
        loadUser()
    }

    fun loadUser() = withLoading{
        safeCall { userUseCase.getUserFromServer() }.handleResponse {
            emitState(ProfileState.Content(it))
        }
    }

    fun exit() = withLoading{
        userUseCase.exit()
        emitState(ProfileState.Exit)
    }

    fun saveChanges(
        username: String,
        email: String,
        legalInfo: LegalInfo? = null,
        physicalInfo: PhysicalInfo? = null
    ) {
        val currentUser = when (val s = state.value) {
            is ProfileState.Content -> s.user
            is ProfileState.SuccessSave -> s.user
            else -> null
        } ?: return

        if (currentUser.username == username &&
            currentUser.email == email &&
            currentUser.legalInfo == legalInfo &&
            currentUser.physicalInfo == physicalInfo
        ) return

        withLoading {
            val request = UpdateUserRequest(
                username = username,
                email = email,
                legalInfo = legalInfo,
                physicalInfo = physicalInfo
            )
            safeCall { userUseCase.updateUser(currentUser.id, request) }
                .handleResponse {
                    emitState(ProfileState.SuccessSave(it))
                }
        }
    }
}
