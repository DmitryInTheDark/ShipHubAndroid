package com.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<S : BaseState, VM : BaseViewModel<S>, VB : ViewBinding>: Fragment() {

    protected abstract val viewModel: VM
    private var _binding: VB? = null
    protected val binding
        get() = _binding!!

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

    abstract fun setupObservers()
    abstract fun setupListeners()
    abstract fun setupUI()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
        setupObservers()
    }

    protected fun showToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    protected fun showToast(stringId: Int) = Toast.makeText(requireContext(), stringId, Toast.LENGTH_SHORT).show()

    protected fun navigate(destination: Int) = findNavController().navigate(destination)
    protected fun navigate(direction: NavDirections) = findNavController().navigate(direction)
    protected fun navigateBack() = findNavController().navigateUp()
}