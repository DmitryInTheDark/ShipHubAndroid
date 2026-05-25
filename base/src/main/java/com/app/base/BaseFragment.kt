package com.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.app.base.ViewUtils.requireLoadingDialog
import com.app.base.databinding.DialogBinding
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding, S : BaseState, VM : BaseViewModel<S>>: Fragment() {

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
        return binding.root
    }

    protected open fun cancelAction(){
        viewModel.cancelAction()
    }

    protected open fun setupObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.simpleState.collect { state ->
                        handleSimpleState(state)
                    }
                }
                launch {
                    viewModel.state.collect { state ->
                        handleState(state)
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
            is SimpleStates.Init -> {}
            is SimpleStates.Loading ->  setLoadingState(state.isLoading)
            is SimpleStates.Error -> setErrorState(state.message)
        }
    }
    protected open fun setErrorState(message: String) {
        createBaseDialog(
            getString(R.string.error),
            message
        ).show()
    }

    private fun createBaseDialog(
        title: String,
        message: String,
        onBtnAction: (() -> Unit)? = null
    ): AlertDialog {

        val dialogBinding = DialogBinding.inflate(layoutInflater)

        dialogBinding.tvTitle.text = title
        dialogBinding.tvMessage.text = message

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnAction.setOnClickListener {
            onBtnAction?.invoke()
            dialog.dismiss()
        }

        return dialog
    }

    protected open fun showDialog(
        title: String,
        message: String,
        onBtnAction: () -> Unit
    ) {
        createBaseDialog(
            title,
            message,
            onBtnAction
        ).show()
    }

    abstract fun handleState(state: S)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupUI()
        setupListeners()
    }

    protected fun showToast(message: String) = Toast
        .makeText(requireContext(), message, Toast.LENGTH_SHORT)
        .show()
//    protected fun showToast(@StringRes stringId: Int) = Toast.makeText(requireContext(), stringId, Toast.LENGTH_SHORT).show()

        protected fun navigate(destination: Int) = findNavController().navigate(destination)
    protected fun navigate(direction: NavDirections) = findNavController().navigate(direction)
    protected fun navigateBack() = findNavController().navigateUp()

    fun Int.dpToPx(): Float{
        val scale = resources.displayMetrics.density
        return this*scale + 0.5f
    }
}