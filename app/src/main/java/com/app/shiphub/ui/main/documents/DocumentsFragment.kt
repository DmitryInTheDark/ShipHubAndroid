package com.app.shiphub.ui.main.documents

import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.shiphub.databinding.FragmentInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocumentsFragment: BaseFragment<FragmentInfoBinding, DocumentsState, DocumentsViewModel>() {

    override val viewModel: DocumentsViewModel by viewModels()

    override fun initializeBinding() = FragmentInfoBinding.inflate(layoutInflater)

    override fun setupListeners() {

    }

    override fun setupUI() {

    }

    override fun handleState(state: DocumentsState) {

    }
}