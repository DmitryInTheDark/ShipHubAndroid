package com.app.shiphub.ui.auth.restore

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.base.BaseFragment
import com.app.data.models.request.RestorePasswordDTO
import com.app.shiphub.R
import com.app.shiphub.databinding.FragmentRestorePasswordFinishBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestorePasswordFinishFragment : BaseFragment<FragmentRestorePasswordFinishBinding, RestorePasswordUIState, RestorePasswordViewModel>() {

    override val viewModel: RestorePasswordViewModel by viewModels()

    override fun initializeBinding() = FragmentRestorePasswordFinishBinding.inflate(layoutInflater)

    private val args: RestorePasswordFinishFragmentArgs by navArgs()

    override fun setupListeners() = with(binding) {
        btnApply.setOnClickListener {
            val password = etPassword.text.toString()
            val repeatPassword = etRepeatPassword.text.toString()
            
            if (password.isEmpty()) {
                showToast(getString(R.string.empty_field))
                return@setOnClickListener
            }
            
            if (password != repeatPassword) {
                showToast(getString(R.string.passwords_dont_match))
                return@setOnClickListener
            }
            
            viewModel.restorePassword(
                RestorePasswordDTO(
                    email = args.email,
                    token = args.token,
                    password = password
                )
            )
        }
    }

    override fun setupUI() {}

    override fun handleState(state: RestorePasswordUIState) {
        when (state) {
            is RestorePasswordUIState.PasswordRestored -> {
                findNavController().setGraph(R.navigation.graph_main_container)
            }
            else -> {}
        }
    }
}
