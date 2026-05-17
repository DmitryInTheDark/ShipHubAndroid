package com.app.shiphub.ui.main.info

import com.app.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(

): BaseViewModel<InfoState>(InfoState.InitScreen()) {


}