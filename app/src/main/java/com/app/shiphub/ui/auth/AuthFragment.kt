package com.app.shiphub.ui.auth

import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.shiphub.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : BaseFragment<AuthUIState, AuthViewModel, FragmentAuthBinding>() {

    override val viewModel: AuthViewModel by viewModels()

    override fun initializeBinding() = FragmentAuthBinding.inflate(layoutInflater)

    override fun setupObservers() {
        super.setupObservers()
//        lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
//                viewModel.state.collectLatest { state ->
//                    when (state) {
//                        is SimpleStates.Error -> showToast(state.message)
//                        is SimpleStates.Loading -> {
//                            Timber.i("loading")
//                        }
//                        else -> {
//                            Timber.i(state.javaClass.simpleName)
//                        }
//                    }
//                }
//            }
//        }
    }

    override fun setupListeners() = with(binding) {
        btnLogin.setOnClickListener {

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