package com.app.shiphub

import com.app.base.SimpleStates
import com.app.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): BaseViewModel<SimpleStates>() {
}