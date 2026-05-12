package com.app.shiphub.ui.auth

import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.base.BaseFragment
import com.app.base.BaseState
import com.app.shiphub.databinding.FragmentAuthBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AuthFragment : BaseFragment<BaseState, AuthViewModel, FragmentAuthBinding>() {

    override val viewModel: AuthViewModel by viewModels()

    override fun initializeBinding() = FragmentAuthBinding.inflate(layoutInflater)

    override fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginSuccess.collectLatest {
                findNavController().navigate(AuthFragmentDirections.actionGlobalMainContainerFragment())
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is BaseState.Error -> showToast(state.message)
                    is BaseState.Loading -> {

                    }
                    else -> {}
                }
            }
        }
    }

    override fun setupListeners() = with(binding) {
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                showToast("Пожалуйста, заполните все поля")
            }
        }
        tvRegistration.setOnClickListener {
            navigate(AuthFragmentDirections.actionAuthFragmentToRegistrationFragment())
        }
    }

    override fun setupUI() {

    }

}