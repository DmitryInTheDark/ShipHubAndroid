package com.app.shiphub.ui.auth

import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.shiphub.databinding.FragmentAuthBinding
import com.app.shiphub.util.BaseValidator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : BaseFragment<AuthUIState, AuthViewModel, FragmentAuthBinding>() {

    override val viewModel: AuthViewModel by viewModels()

    override fun initializeBinding() = FragmentAuthBinding.inflate(layoutInflater)

    override fun setupListeners() = with(binding) {
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            val emailErrors = BaseValidator.validateEmail(email)
            if (emailErrors.isNotEmpty()) etEmail.error = emailErrors.first()

            val passwordErrors = BaseValidator.validatePassword(password)
            if (passwordErrors.isNotEmpty()) etPassword.error = passwordErrors.first()

            if (emailErrors.isEmpty() && passwordErrors.isEmpty()) viewModel.login(email, password)
        }
        tvRegistration.setOnClickListener {
            navigate(AuthFragmentDirections.actionAuthFragmentToRegistrationFragment())
        }
    }

    override fun setupUI() {

    }

    override fun handleState(state: AuthUIState) {

    }

}