package com.app.shiphub

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import com.app.base.BaseActivity
import com.app.shiphub.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainUIState, MainViewModel, ActivityMainBinding>() {

    override val viewModel: MainViewModel by viewModels()
    private val navController by lazy { findNavController(binding.main.id) }

    override fun initializeBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setupListeners() {

    }

    override fun setupUI() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = false
        @Suppress("DEPRECATION")
        window.statusBarColor = baseContext.getColor(R.color.black)
    }

    override fun handleState(state: MainUIState) {
        when(state){
            is MainUIState.SkipLogin -> {
                navController.setGraph(R.navigation.graph_main_container)
            }
            is MainUIState.ShowLoginScreen -> {
                navController.setGraph(R.navigation.graph_auth)
            }
        }
    }

}