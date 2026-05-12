package com.app.shiphub.ui.auth

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.base.BaseFragment
import com.app.base.SimpleStates
import com.app.shiphub.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AuthFragment : BaseFragment<AuthUIState, AuthViewModel, FragmentAuthBinding>() {

    override val viewModel: AuthViewModel by viewModels()

    override fun initializeBinding() = FragmentAuthBinding.inflate(layoutInflater)

    override fun setupObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collectLatest { state ->
                    when (state) {
                        is SimpleStates.Error -> showToast(state.message)
                        is SimpleStates.Loading -> {
                            Timber.i("loading")
                        }
                        else -> {
                            Timber.i(state.javaClass.simpleName)
                        }
                    }
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