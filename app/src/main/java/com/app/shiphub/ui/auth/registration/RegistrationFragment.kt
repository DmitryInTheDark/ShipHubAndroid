package com.app.shiphub.ui.auth.registration

import android.view.KeyEvent
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.base.SimpleStates
import com.app.data.models.enums.UserType
import com.app.shiphub.databinding.FragmentRegistrationBinding
import com.app.shiphub.util.BaseValidator
import com.google.android.material.tabs.TabLayout
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RegistrationFragment: BaseFragment<SimpleStates, RegistrationViewModel, FragmentRegistrationBinding>() {

    override val viewModel: RegistrationViewModel by viewModels()
    override fun initializeBinding() = FragmentRegistrationBinding.inflate(layoutInflater)
    private var currentUserType = UserType.LEGAL
    private val legalErrors: List<String>
        get() = with(binding) {
            listOf(etOrgName.error.toString(), etInn.error.toString(),
                etKpp.error.toString(), etLegalAddress.error.toString(),
                etFio.error.toString(), etPhone.error.toString(),
                etEmailLegal.error.toString(), etPasswordLegal.error.toString()
            )
        }

    override fun setupListeners() = with(binding) {
        tvAuth.setOnClickListener { navigateBack() }
        btnRegister.setOnClickListener {
            validateFields()
            Timber.i(etPhone.text.toString())
        }
    }

    private fun validateFields() = with(binding){
        if (currentUserType == UserType.LEGAL){
            etOrgName.error = BaseValidator.validateOrgName(etOrgName.text.toString()).first()
            etInn.error = BaseValidator.validateInn(etInn.text.toString()).first()
            etKpp.error = BaseValidator.validateKpp(etKpp.text.toString()).first()
            etLegalAddress.error = BaseValidator.validateAddress(etLegalAddress.text.toString()).first()
            etFio.error = BaseValidator.validateFio(etAuthorizedPerson.text.toString()).first()
            etPhone.error = BaseValidator.validatePhone(etPhone.text.toString()).first()
            etEmailLegal.error =BaseValidator.validateEmail(etEmailLegal.text.toString()).first()
            etPasswordLegal.error = BaseValidator.validatePassword(etPasswordLegal.text.toString()).first()
            if (legalErrors.none { it.isBlank() }){
                viewModel.registerLegal(
//                    LegalInfo(
//                        etOrgName.text.toString(),
//                        etInn.text.toString(),
//                        etKpp.text.toString(),
//                        etLegalAddress.text.toString(),
//                        etPhone.text.toString()
//                    )
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

    override fun handleState(state: SimpleStates) {}
}
