package com.app.shiphub

import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.app.base.BaseActivity
import com.app.shiphub.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainUIState, MainViewModel, ActivityMainBinding>() {

    override val viewModel: MainViewModel by viewModels()
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main) as NavHostFragment
        navHostFragment.navController
    }

    override fun initializeBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setupListeners() {

    }

    override fun setupUI() {

    }

    override fun handleState(state: MainUIState) {
        when(state){
            is MainUIState.SkipLogin -> {
                navController.setGraph(R.navigation.graph_main_container)
            }
            is MainUIState.ShowLoginScreen -> {
                navController.setGraph(R.navigation.graph_auth)
            }
            is MainUIState.InitScreen -> {}
        }
    }

}