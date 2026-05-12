package com.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.app.base.ViewUtils.requireLoadingDialog
import kotlinx.coroutines.launch
import kotlin.getValue

abstract class BaseFragment<S : BaseState, VM : BaseViewModel<S>, VB : ViewBinding>: Fragment() {

    protected abstract val viewModel: VM
    private var _binding: VB? = null
    protected val binding
        get() = _binding!!
    protected val loadingDialog by lazy {
        requireActivity().requireLoadingDialog(true){
            cancelAction()
        }
    }

    protected abstract fun initializeBinding(): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = initializeBinding()
        binding.root.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
        return binding.root
    }

    protected open fun cancelAction(){
        viewModel.cancelAction()
    }

    protected open fun setupObservers(){
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
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
    abstract fun setupListeners()
    abstract fun setupUI()
    protected open fun setLoadingState(isLoading: Boolean){
        if (isLoading) loadingDialog.show() else loadingDialog.dismiss()
    }

    protected open fun handleSimpleState(state: SimpleStates){
        when(state){
            is SimpleStates.Init -> setupUI()
            is SimpleStates.Loading ->  setLoadingState(state.isLoading)
            is SimpleStates.Error -> showToast(state.message)
        }
    }

    abstract fun handleState(state: S)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
        setupObservers()
    }

    protected fun showToast(message: String) = Toast
        .makeText(requireContext(), message, Toast.LENGTH_SHORT)
        .show()
    protected fun showToast(@StringRes stringId: Int) = Toast.makeText(requireContext(), stringId, Toast.LENGTH_SHORT).show()

//    protected fun navigate(destination: Int) = findNavController().navigate(destination)
    protected fun navigate(direction: NavDirections) = findNavController().navigate(direction)
    protected fun navigateBack() = findNavController().navigateUp()
}