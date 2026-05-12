package com.app.shiphub.ui.auth.registration

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.base.BaseFragment
import com.app.base.SimpleStates
import com.app.base.models.auth.PhysicalInfo
import com.app.base.models.auth.RegistrationRequestDTO
import com.app.domain.UserType
import com.app.shiphub.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment: BaseFragment<SimpleStates, RegistrationViewModel, FragmentRegistrationBinding>() {

    override val viewModel: RegistrationViewModel by viewModels()
    override fun initializeBinding() = FragmentRegistrationBinding.inflate(layoutInflater)
    private var currentUserType = UserType.LEGAL

    override fun setupObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.registrationSuccess.collectLatest {
                    val request = if (currentUserType == UserType.PHYSICAL) {
                        RegistrationRequestDTO(
                            name = binding.etFio.text.toString(),
                            email = binding.etEmailPhysical.text.toString(),
                            password = binding.etPasswordPhysical.text.toString(),
                            type = UserType.PHYSICAL,
                            physicalInfo = PhysicalInfo(
                                address = binding.etLivingAddress.text.toString()
                            )
                        )
                    } else {
                        RegistrationRequestDTO(
                            name = binding.etOrgName.text.toString(),
                            email = binding.etEmailLegal.text.toString(),
                            password = binding.etPasswordLegal.text.toString(),
                            type = UserType.LEGAL
                        )
                    }
                    navigate(RegistrationFragmentDirections.actionRegistrationFragmentToEmailFragment(request))
                }
            }
        }
    }

    override fun setupListeners() = with(binding) {
        binding.tabLayout.addOnTabSelectedListener(object :
            com.google.android.material.tabs.TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab) {
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

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
        tvAuth.setOnClickListener { navigateBack() }
        btnRegister.setOnClickListener {
            val request = if (currentUserType == UserType.PHYSICAL) {
                RegistrationRequestDTO(
                    name = etFio.text.toString(),
                    email = etEmailPhysical.text.toString(),
                    password = etPasswordPhysical.text.toString(),
                    type = UserType.PHYSICAL,
                    physicalInfo = PhysicalInfo(
                        address = etLivingAddress.text.toString()
                    )
                )
            } else {
                RegistrationRequestDTO(
                    name = etOrgName.text.toString(),
                    email = etEmailLegal.text.toString(),
                    password = etPasswordLegal.text.toString(),
                    type = UserType.LEGAL
                )
            }
            viewModel.register(request)
        }
    }

    override fun setupUI() {
    }
}
