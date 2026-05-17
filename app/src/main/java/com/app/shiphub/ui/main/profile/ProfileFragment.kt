package com.app.shiphub.ui.main.profile

import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.shiphub.databinding.FragmentInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment: BaseFragment<FragmentInfoBinding, ProfileState, ProfileViewModel>() {

    override val viewModel: ProfileViewModel by viewModels()

    override fun initializeBinding() = FragmentInfoBinding.inflate(layoutInflater)

    override fun setupListeners() {

    }

    override fun setupUI() {

    }

    override fun handleState(state: ProfileState) {

    }
}