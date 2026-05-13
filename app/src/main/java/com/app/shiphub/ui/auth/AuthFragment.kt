package com.app.shiphub.ui.auth

import android.text.method.PasswordTransformationMethod
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.base.BaseFragment
import com.app.shiphub.R
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
        tilPassword.setEndIconOnClickListener {

            val passwordIsVisible =
                etPassword.transformationMethod == null

            etPassword.transformationMethod =
                if (passwordIsVisible) PasswordTransformationMethod.getInstance()
                else null

            val endIcon = AppCompatResources.getDrawable(
                requireContext(),
                if (passwordIsVisible) {
                    R.drawable.password_is_visible
                } else {
                    R.drawable.password_not_visible
                }
            )
            tilPassword.endIconDrawable = endIcon

            etPassword.setSelection(etPassword.text?.length ?: 0)
        }
    }

    override fun setupUI() {

    }

    override fun handleState(state: AuthUIState) {
        when(state){
            is AuthUIState.SuccessLogin -> findNavController().setGraph(R.navigation.graph_main_container)
        }
    }

}