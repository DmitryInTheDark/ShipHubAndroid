package com.app.shiphub.ui.main.profile

import android.view.KeyEvent
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.data.models.domain.LegalInfo
import com.app.data.models.domain.PhysicalInfo
import com.app.data.models.domain.User
import com.app.data.models.enums.UserType
import com.app.shiphub.MainActivity
import com.app.shiphub.R
import com.app.shiphub.databinding.FragmentProfileBinding
import com.app.shiphub.util.BaseValidator
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment: BaseFragment<FragmentProfileBinding, ProfileState, ProfileViewModel>() {

    override val viewModel: ProfileViewModel by viewModels()

    override fun initializeBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun setupListeners() = with(binding) {
        btnSave.setOnClickListener {
            if (validateFields()) {
                val user = when (val s = viewModel.state.value) {
                    is ProfileState.Content -> s.user
                    is ProfileState.SuccessSave -> s.user
                    else -> null
                }
                user?.let {
                    collectAndSaveData(it.type)
                }
            }
        }
        btnExit.setOnClickListener {
            showDialog(
                requireContext().getString(R.string.allow_exit_title),
                requireContext().getString(R.string.allow_exit)
            ){
                viewModel.exit()
            }
        }
        srlMain.setOnRefreshListener {
            viewModel.loadUser()
        }
    }

    private fun collectAndSaveData(type: UserType) = with(binding) {
        when (type) {
            UserType.LEGAL -> {
                viewModel.saveChanges(
                    username = etRepFio.text.toString(),
                    email = etLegalEmail.text.toString(),
                    legalInfo = LegalInfo(
                        organizationName = etOrgName.text.toString(),
                        inn = etInn.text.toString(),
                        kpp = etKpp.text.toString(),
                        address = etLegalAddress.text.toString(),
                        phone = etRepPhone.text.toString()
                    )
                )
            }
            UserType.PHYSICAL -> {
                viewModel.saveChanges(
                    username = etPhysFio.text.toString(),
                    email = etEmail.text.toString(),
                    physicalInfo = PhysicalInfo(
                        address = etLivingAddress.text.toString()
                    )
                )
            }
            UserType.MANAGER -> {
                viewModel.saveChanges(
                    username = etName.text.toString(),
                    email = etManagerEmail.text.toString()
                )
            }
            else -> {}
        }
    }

    override fun setLoadingState(isLoading: Boolean) = with(binding){
        super.setLoadingState(isLoading)
        srlMain.isRefreshing = isLoading
    }

    private fun validateFields(): Boolean {
        clearErrors()

        return when {
            binding.llLegal.isVisible -> validateLegalFields()
            binding.llPhysical.isVisible -> validatePhysicalFields()
            binding.llManager.isVisible -> validateManagerFields()
            else -> false
        }
    }

    private fun clearErrors() = with(binding) {
        val layouts = listOf(
            tilOrgName,
            tilInn,
            tilKpp,
            tilLegalAddress,
            tilRepFio,
            tilRepPhone,
            tilLegalEmail,
            tilPhysFio,
            tilLivingAddress,
            tilEmail,
            tilName,
            tilManagerEmail
        )

        layouts.forEach { it.error = null }
    }

    private fun validateLegalFields(): Boolean = with(binding) {

        BaseValidator.validateOrgName(etOrgName.text.toString()).apply {
            tilOrgName.error = if (isNotEmpty()) first() else null
        }

        BaseValidator.validateInn(etInn.text.toString()).apply {
            tilInn.error = if (isNotEmpty()) first() else null
        }

        BaseValidator.validateKpp(etKpp.text.toString()).apply {
            tilKpp.error = if (isNotEmpty()) first() else null
        }

        BaseValidator.validateAddress(etLegalAddress.text.toString()).apply {
            tilLegalAddress.error = if (isNotEmpty()) first() else null
        }

        BaseValidator.validateFio(etRepFio.text.toString()).apply {
            tilRepFio.error = if (isNotEmpty()) first() else null
        }

        BaseValidator.validatePhone(etRepPhone.text.toString()).apply {
            tilRepPhone.error = if (isNotEmpty()) first() else null
        }

        BaseValidator.validateEmail(etLegalEmail.text.toString()).apply {
            tilLegalEmail.error = if (isNotEmpty()) first() else null
        }

        listOf(
            tilOrgName.error,
            tilInn.error,
            tilKpp.error,
            tilLegalAddress.error,
            tilRepFio.error,
            tilRepPhone.error,
            tilLegalEmail.error
        ).all { it.isNullOrBlank() }
    }

    private fun validatePhysicalFields(): Boolean = with(binding) {

        BaseValidator.validateFio(etPhysFio.text.toString()).apply {
            tilPhysFio.error = if (isNotEmpty()) first() else null
        }

        BaseValidator.validateAddress(etLivingAddress.text.toString()).apply {
            tilLivingAddress.error = if (isNotEmpty()) first() else null
        }

        BaseValidator.validateEmail(etEmail.text.toString()).apply {
            tilEmail.error = if (isNotEmpty()) first() else null
        }

        listOf(
            tilPhysFio.error,
            tilLivingAddress.error,
            tilEmail.error
        ).all { it.isNullOrBlank() }
    }

    private fun validateManagerFields(): Boolean = with(binding) {

        BaseValidator.validateFio(etName.text.toString()).apply {
            tilName.error = if (isNotEmpty()) first() else null
        }

        BaseValidator.validateEmail(etManagerEmail.text.toString()).apply {
            tilManagerEmail.error = if (isNotEmpty()) first() else null
        }

        listOf(
            tilName.error,
            tilManagerEmail.error
        ).all { it.isNullOrBlank() }
    }

    private fun setupLegalInfo(user: User) = with(binding){
        llLegal.isVisible = true
        llPhysical.isVisible = false
        llManager.isVisible = false
        user.legalInfo?.let { legalInfo ->
            etOrgName.setText(legalInfo.organizationName)
            etInn.setText(legalInfo.inn)
            etKpp.setText(legalInfo.kpp)
            etLegalAddress.setText(legalInfo.address)
            etRepPhone.setText(legalInfo.phone)
        }
        etRepFio.setText(user.username)
        etLegalEmail.setText(user.email)
    }

    private fun setupPhysicalInfo(user: User) = with(binding){
        llPhysical.isVisible = true
        llLegal.isVisible = false
        llManager.isVisible = false

        etPhysFio.setText(user.username)
        etEmail.setText(user.email)

        user.physicalInfo?.let { physicalInfo ->
            etLivingAddress.setText(physicalInfo.address)
        }
    }

    private fun setupManagerInfo(user: User) = with(binding){
        llManager.isVisible = true
        llPhysical.isVisible = false
        llLegal.isVisible = false

        etName.setText(user.username)
        Timber.i(user.email)
        etManagerEmail.setText(user.email)
    }

    override fun setupUI() = with(binding){
        val listener = MaskedTextChangedListener(
            "+7 ([000]) [000]-[00]-[00]",
            etRepPhone
        )
        etRepPhone.setOnKeyListener { _, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_DEL &&
                    etRepPhone.selectionStart <= 4 &&
                    event.action == KeyEvent.ACTION_DOWN
        }
        etRepPhone.addTextChangedListener(listener)
        etRepPhone.onFocusChangeListener = listener
    }

    override fun handleState(state: ProfileState) {
        when(state){
            is ProfileState.Content -> {
                validateUserByType(state.user)
            }
            is ProfileState.SuccessSave -> {
                validateUserByType(state.user)
                showToast(getString(R.string.success_save))
            }
            is ProfileState.Init -> {}
            is ProfileState.Exit -> {
                exit()
            }
        }
    }

    private fun exit(){
        (requireActivity() as MainActivity).exit()
    }

    private fun validateUserByType(user: User) {
        when(user.type){
            UserType.LEGAL -> setupLegalInfo(user)
            UserType.PHYSICAL -> setupPhysicalInfo(user)
            UserType.MANAGER -> setupManagerInfo(user)
            UserType.UNKNOWN -> throw IllegalStateException("Неизвестный тип пользователя: UserType - ${user.type}")
        }
    }
}