package com.app.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<S : BaseState, VM : BaseViewModel<S>, VB : ViewBinding>: AppCompatActivity() {

    protected abstract val viewModel: VM
    private var _binding: VB? = null
    protected val binding
        get() = _binding!!

    protected abstract fun initializeBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = initializeBinding()
        setContentView(binding.root)
        setupUI()
        setupListeners()
        setupObservers()
    }

    abstract fun setupObservers()
    abstract fun setupListeners()
    abstract fun setupUI()

    protected fun showToast(message: String) = Toast.makeText(baseContext, message, Toast.LENGTH_SHORT)
    protected fun showToast(stringId: Int) = Toast.makeText(baseContext, stringId, Toast.LENGTH_SHORT)
}