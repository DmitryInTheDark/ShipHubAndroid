package com.app.shiphub.ui.main.profile

import com.app.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

): BaseViewModel<ProfileState>(ProfileState.InitScreen()) {


}