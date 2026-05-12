package com.app.shiphub.ui.auth.email

import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.app.base.BaseFragment
import com.app.base.BaseState
import com.app.shiphub.R
import com.app.shiphub.databinding.FragmentEmailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class EmailFragment : BaseFragment<BaseState, EmailViewModel, FragmentEmailBinding>() {

    override val viewModel: EmailViewModel by viewModels()

    private val args: EmailFragmentArgs by navArgs()

    override fun initializeBinding() = FragmentEmailBinding.inflate(layoutInflater)

    override fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.verifySuccess.collectLatest {
                navigate(EmailFragmentDirections.actionGlobalMainContainerFragment())
            }
        }
    }

    override fun setupListeners() = with(binding) {
        val otps = listOf(etOtp1, etOtp2, etOtp3, etOtp4)

        otps.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        if (index < otps.size - 1) {
                            otps[index + 1].requestFocus()
                        }
                    }
                    validateCode()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && event.action == android.view.KeyEvent.ACTION_DOWN) {
                    if (editText.text.isEmpty() && index > 0) {
                        otps[index - 1].apply {
                            text = null
                            requestFocus()
                        }
                    }
                }
                false
            }
        }

        btnConfirm.setOnClickListener {
            val code = etOtp1.text.toString() + etOtp2.text.toString() + etOtp3.text.toString() + etOtp4.text.toString()
            viewModel.verifyCode(args.registrationRequest.email, code)
        }
        tvResendCode.setOnClickListener { viewModel.resendCode(args.registrationRequest) }
    }

    private fun validateCode() {
        val code = with(binding) {
            etOtp1.text.toString() + etOtp2.text.toString() + etOtp3.text.toString() + etOtp4.text.toString()
        }
        binding.btnConfirm.isVisible = code.length == 4
    }

    override fun setupUI() = with(binding){
        tvTitle.text = context?.getString(R.string.code_send_on_email, args.registrationRequest.email)
    }

}
