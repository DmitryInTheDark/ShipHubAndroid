package com.app.shiphub.ui.main.home

import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.base.SimpleStates
import com.app.base.BaseViewModel
import com.app.shiphub.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<SimpleStates>()

@AndroidEntryPoint
class HomeFragment : BaseFragment<SimpleStates, HomeViewModel, FragmentHomeBinding>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun initializeBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun setupObservers() {}

    override fun setupListeners() {}

    override fun setupUI() {}
}
