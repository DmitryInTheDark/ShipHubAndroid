package com.app.shiphub.ui.auth.registration

import android.view.KeyEvent
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.data.models.domain.LegalInfo
import com.app.data.models.domain.PhysicalInfo
import com.app.data.models.domain.BaseUserInfo
import com.app.data.models.enums.UserType
import com.app.shiphub.databinding.FragmentRegistrationBinding
import com.app.shiphub.util.BaseValidator
import com.google.android.material.tabs.TabLayout
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RegistrationFragment: BaseFragment<RegistrationUIState, RegistrationViewModel, FragmentRegistrationBinding>() {

    override val viewModel: RegistrationViewModel by viewModels()
    override fun initializeBinding() = FragmentRegistrationBinding.inflate(layoutInflater)
    private var currentUserType = UserType.LEGAL
    private val legalErrors
        get() = with(binding) {
            listOf(etOrgName.error, etInn.error,
                etKpp.error, etLegalAddress.error,
                etAuthorizedPerson.error, etPhone.error,
                etEmailLegal.error, etPasswordLegal.error) }
    private val physicalErrors
        get() = with(binding) {
            listOf(etFio.error, etLivingAddress.error,
                etEmailPhysical.error, etPasswordPhysical.error) }

    override fun setupListeners() = with(binding) {
        tvAuth.setOnClickListener { navigateBack() }
        btnRegister.setOnClickListener {
            validateFields()
            Timber.i(etPhone.text.toString())
        }
    }

    private fun validateFields() = with(binding){
        if (currentUserType == UserType.LEGAL){
            BaseValidator.validateOrgName(etOrgName.text.toString()).apply {
                etOrgName.error = if (isNotEmpty()) first() else null
            }
            BaseValidator.validateInn(etInn.text.toString()).apply {
                etInn.error = if (isNotEmpty()) first() else null
            }
            BaseValidator.validateKpp(etKpp.text.toString()).apply {
                etKpp.error = if (isNotEmpty()) first() else null
            }
            BaseValidator.validateAddress(etLegalAddress.text.toString()).apply {
                etLegalAddress.error = if (isNotEmpty()) first() else null
            }
            BaseValidator.validateFio(etAuthorizedPerson.text.toString()).apply {
                etAuthorizedPerson.error = if (isNotEmpty()) first() else null
            }
            BaseValidator.validatePhone(etPhone.text.toString()).apply {
                etPhone.error = if (isNotEmpty()) first() else null
            }
            BaseValidator.validateEmail(etEmailLegal.text.toString()).apply {
                etEmailLegal.error = if (isNotEmpty()) first() else null
            }
            BaseValidator.validatePassword(etPasswordLegal.text.toString()).apply {
                etPasswordLegal.error = if (isNotEmpty()) first() else null
            }
            if (legalErrors.all { it.isNullOrBlank() }){
                viewModel.registerLegal(
                    BaseUserInfo(
                        etAuthorizedPerson.text.toString(),
                        etEmailLegal.text.toString(),
                        UserType.LEGAL,
                        etPasswordLegal.text.toString()
                    ),
                    LegalInfo(
                        etOrgName.text.toString(),
                        etInn.text.toString(),
                        etKpp.text.toString(),
                        etLegalAddress.text.toString(),
                        etPhone.text.toString()
                    )
                )
            }
        }
        if (currentUserType == UserType.PHYSICAL){
            BaseValidator.validateFio(etFio.text.toString()).apply {
                etFio.error = if (isNotEmpty()) first() else null
            }
            BaseValidator.validateAddress(etLivingAddress.text.toString()).apply {
                etLivingAddress.error = if (isNotEmpty()) first() else null
            }
            BaseValidator.validateEmail(etEmailPhysical.text.toString()).apply {
                etEmailPhysical.error = if (isNotEmpty()) first() else null
            }
            BaseValidator.validatePassword(etPasswordPhysical.text.toString()).apply {
                etPasswordPhysical.error = if (isNotEmpty()) first() else null
            }
            if (physicalErrors.all { it.isNullOrBlank() }){
                viewModel.registerPhysical(
                    BaseUserInfo(
                        etFio.text.toString(),
                        etEmailPhysical.text.toString(),
                        UserType.PHYSICAL,
                        etPasswordPhysical.text.toString()
                    ),
                    PhysicalInfo(
                        etLivingAddress.text.toString()
                    )
                )
            }
        }

    }

    private fun synchronizeTabLayout(){
        binding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        currentUserType = UserType.LEGAL
                        binding.llLegal.isVisible = true
                        binding.llPhysical.isVisible = false
                    }
                    1 -> {
                        currentUserType = UserType.PHYSICAL
                        binding.llLegal.isVisible = false
                        binding.llPhysical.isVisible = true
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun setupUI() = with(binding){
        synchronizeTabLayout()
        val listener = MaskedTextChangedListener(
            "+7 ([000]) [000]-[00]-[00]",
            etPhone
        )
        etPhone.setOnKeyListener { _, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_DEL &&
            etPhone.selectionStart <= 4 &&
            event.action == KeyEvent.ACTION_DOWN
        }
        etPhone.addTextChangedListener(listener)
        etPhone.onFocusChangeListener = listener

    }

    override fun handleState(state: RegistrationUIState) {
        when(state){
            is RegistrationUIState.SuccessRegistration -> navigate(
                RegistrationFragmentDirections.actionRegistrationFragmentToEmailFragment(state.email)
            )
            is RegistrationUIState.InitScreen -> {}
        }
    }
}
