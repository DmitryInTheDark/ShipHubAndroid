package com.app.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.app.base.ViewUtils.requireLoadingDialog
import kotlinx.coroutines.launch

abstract class BaseActivity<S : BaseState, VM : BaseViewModel<S>, VB : ViewBinding>: AppCompatActivity() {

    protected abstract val viewModel: VM
    private var _binding: VB? = null
    protected val binding
        get() = _binding!!

    protected val loadingDialog by lazy {
        requireLoadingDialog(true){
            cancelAction()
        }
    }

    protected abstract fun initializeBinding(): VB

    protected open fun setupObservers(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collect { state ->
                    when(state){
                        is SimpleStates -> handleSimpleState(state)
                        else -> {
                            try {
                                @Suppress("UNCHECKED_CAST")
                                handleState(state as S)
                            }catch (e : ClassCastException){
                                showToast(R.string.unknown_error)
                            }
                        }
                    }
                }
            }
        }
    }

    protected open fun handleSimpleState(state: SimpleStates){
        when(state){
            is SimpleStates.Init -> setupUI()
            is SimpleStates.Loading ->  setLoadingState(state.isLoading)
            is SimpleStates.Error -> showToast(state.message)
        }
    }

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

    protected open fun setLoadingState(isLoading: Boolean){
        if (isLoading) loadingDialog.show() else loadingDialog.dismiss()
    }

    abstract fun handleState(state: S)

    abstract fun setupListeners()
    abstract fun setupUI()

    protected open fun cancelAction(){
        viewModel.cancelAction()
    }

    protected fun showToast(message: String) = Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
    protected fun showToast(@StringRes stringId: Int) = Toast.makeText(baseContext, stringId, Toast.LENGTH_SHORT).show()
}