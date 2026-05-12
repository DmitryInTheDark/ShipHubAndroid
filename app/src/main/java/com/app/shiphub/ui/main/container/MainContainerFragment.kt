package com.app.shiphub.ui.main.container

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.app.base.BaseFragment
import com.app.base.SimpleStates
import com.app.base.BaseViewModel
import com.app.shiphub.R
import com.app.shiphub.databinding.FragmentMainContainerBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainContainerViewModel @Inject constructor() : BaseViewModel<SimpleStates>()

@AndroidEntryPoint
class MainContainerFragment : BaseFragment<SimpleStates, MainContainerViewModel, FragmentMainContainerBinding>() {

    override val viewModel: MainContainerViewModel by viewModels()

    override fun initializeBinding() = FragmentMainContainerBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun setupObservers() {}
    override fun setupListeners() {}
    override fun setupUI() {}
}
