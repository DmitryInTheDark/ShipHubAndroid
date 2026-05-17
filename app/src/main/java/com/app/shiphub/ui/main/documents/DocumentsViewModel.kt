package com.app.shiphub.ui.main.documents

import com.app.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DocumentsViewModel @Inject constructor(
): BaseViewModel<DocumentsState>(DocumentsState.InitScreen()) {


}