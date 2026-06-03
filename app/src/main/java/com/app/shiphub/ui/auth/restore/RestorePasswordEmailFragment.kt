package com.app.shiphub.ui.auth.restore

import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.shiphub.databinding.FragmentRestorePasswordEmailBinding
import com.app.shiphub.util.BaseValidator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestorePasswordEmailFragment : BaseFragment<FragmentRestorePasswordEmailBinding, RestorePasswordUIState, RestorePasswordViewModel>() {

    override val viewModel: RestorePasswordViewModel by viewModels()

    override fun initializeBinding() = FragmentRestorePasswordEmailBinding.inflate(layoutInflater)

    override fun setupListeners() = with(binding) {
        btnRestore.setOnClickListener {
            val email = etEmail.text.toString()
            BaseValidator.validateEmail(email).apply {
                if (isEmpty()) viewModel.requestRestorePassword(email) else showToast(first())
            }
        }
    }

    override fun setupUI() {}

    override fun handleState(state: RestorePasswordUIState) {
        when (state) {
            is RestorePasswordUIState.RequestSent -> {
                navigate(
                    RestorePasswordEmailFragmentDirections.actionRestorePasswordEmailFragmentToEmailFragment(
                        email = state.email,
                        isRestore = true
                    )
                )
            }
            else -> {}
        }
    }
}
