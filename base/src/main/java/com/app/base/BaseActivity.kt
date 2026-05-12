package com.app.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<S : SimpleStates, VM : BaseViewModel<S>, VB : ViewBinding>: AppCompatActivity() {

    protected abstract val viewModel: VM
    private var _binding: VB? = null
    protected val binding
        get() = _binding!!

    protected abstract fun initializeBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = initializeBinding()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(binding.root.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupUI()
        setupListeners()
        setupObservers()
    }

    abstract fun setupObservers()
    abstract fun setupListeners()
    abstract fun setupUI()

//    protected fun showToast(message: String) = Toast.makeText(baseContext, message, Toast.LENGTH_SHORT)
//    protected fun showToast(stringId: Int) = Toast.makeText(baseContext, stringId, Toast.LENGTH_SHORT)
}