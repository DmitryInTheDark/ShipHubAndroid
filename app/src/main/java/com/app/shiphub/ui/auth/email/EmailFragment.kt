package com.app.shiphub.ui.auth.email

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.app.base.BaseFragment
import com.app.shiphub.R
import com.app.shiphub.databinding.FragmentEmailBinding
import com.app.shiphub.util.BaseValidator
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class EmailFragment : BaseFragment<EmailUIState, EmailViewModel, FragmentEmailBinding>() {

    override val viewModel: EmailViewModel by viewModels()

    override fun initializeBinding() = FragmentEmailBinding.inflate(layoutInflater)

    private val args: EmailFragmentArgs by navArgs()
    private val ets
        get() = with(binding){listOf(etOtp1, etOtp2, etOtp3, etOtp4, etOtp5)}
    private val code
        get() = ets.joinToString("") { it.text.toString() }

    override fun setupListeners() = with(binding) {


        ets.forEachIndexed { index, editText ->

            editText.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
                val regex = BaseValidator.codeRegex
                if (source.isNullOrEmpty()) ""
                else if (regex.matches(source.toString().uppercase())) source.toString().uppercase()
                else ""
            })

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && index < ets.size - 1) {
                        ets[index + 1].requestFocus()
                    }
                    validateCode()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL &&
                    event.action == KeyEvent.ACTION_DOWN &&
                    editText.text.isEmpty() &&
                    index > 0) {

                    ets[index - 1].apply {
                        requestFocus()
                        setText("")
                        setSelection(0)
                    }
                    return@setOnKeyListener true
                }
                false
            }
        }

        btnConfirm.setOnClickListener {
            BaseValidator.validateEmailCode(code).apply {
                if (isEmpty()) viewModel.verifyCode(code, args.email) else showToast(first())
            }
        }
    }

    private fun validateCode() {
        binding.btnConfirm.isEnabled = code.length == 5
    }

    override fun setupUI() = with(binding){
        tvTitle.text = requireContext().getString(R.string.code_send_on_email, args.email)
    }

    override fun handleState(state: EmailUIState) {
        when(state){
            is EmailUIState.EmailVerified -> navigate(
                EmailFragmentDirections.actionEmailFragmentToGraphMainContainer()
            )
            is EmailUIState.InitScreen -> {}
        }
    }

}
