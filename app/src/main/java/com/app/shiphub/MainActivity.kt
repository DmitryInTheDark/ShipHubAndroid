package com.app.shiphub

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.app.base.BaseActivity
import com.app.data.models.enums.UserType
import com.app.shiphub.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainUIState, MainViewModel, ActivityMainBinding>() {

    override val viewModel: MainViewModel by viewModels()
    private val navController
        get() = (supportFragmentManager.findFragmentById(R.id.main) as NavHostFragment).navController

    override fun initializeBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setupListeners() {

    }

    override fun setupUI() {}

    fun exit(){
        navController.setGraph(R.navigation.graph_auth)
    }

    override fun handleState(state: MainUIState) {
        when(state){
            is MainUIState.SkipLogin -> {
                val isManager = state.userType == UserType.MANAGER
                val graphId = if (isManager) R.navigation.graph_manager_container else R.navigation.graph_main_container
                val args = Bundle().apply { putBoolean("isManager", isManager) }
                navController.setGraph(graphId, args)
            }
            is MainUIState.ShowLoginScreen -> {
                navController.setGraph(R.navigation.graph_auth)
            }
            is MainUIState.InitScreen -> {}
        }
    }
}
