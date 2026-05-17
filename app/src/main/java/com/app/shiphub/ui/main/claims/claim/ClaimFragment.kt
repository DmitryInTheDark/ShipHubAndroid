package com.app.shiphub.ui.main.claims.claim

import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.shiphub.databinding.FragmentInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimFragment: BaseFragment<FragmentInfoBinding, ClaimState, ClaimViewModel>() {

    override val viewModel: ClaimViewModel by viewModels()

    override fun initializeBinding() = FragmentInfoBinding.inflate(layoutInflater)

    override fun setupListeners() {

    }

    override fun setupUI() {

    }

    override fun handleState(state: ClaimState) {

    }
}