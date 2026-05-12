package com.app.shiphub

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsControllerCompat
import com.app.base.BaseActivity
import com.app.base.SimpleStates
import com.app.shiphub.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<SimpleStates, MainViewModel, ActivityMainBinding>() {

    override val viewModel: MainViewModel by viewModels()

    override fun initializeBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setupObservers() {

    }

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

}