package com.app.shiphub.ui.main.info

import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.shiphub.databinding.FragmentInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment: BaseFragment<InfoState, InfoViewModel, FragmentInfoBinding>() {

    override val viewModel: InfoViewModel by viewModels()

    override fun initializeBinding() = FragmentInfoBinding.inflate(layoutInflater)

    override fun setupListeners() {

    }

    override fun setupUI() {

    }

    override fun handleState(state: InfoState) {

    }
}