package com.app.shiphub.ui.main.claims.claim

import com.app.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClaimViewModel @Inject constructor(
): BaseViewModel<ClaimState>(ClaimState.InitScreen()) {


}