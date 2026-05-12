package com.app.shiphub.ui.main.home

import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.base.SimpleStates
import com.app.shiphub.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<SimpleStates, HomeViewModel, FragmentHomeBinding>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun initializeBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun setupObservers() {}

    override fun setupListeners() {}

    override fun setupUI() {}

    override fun handleState(state: SimpleStates) {

    }
}
