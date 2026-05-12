package com.app.shiphub.ui.auth.registration

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.app.base.BaseFragment
import com.app.base.SimpleStates
import com.app.domain.UserType
import com.app.shiphub.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment: BaseFragment<SimpleStates, RegistrationViewModel, FragmentRegistrationBinding>() {

    override val viewModel: RegistrationViewModel by viewModels()
    override fun initializeBinding() = FragmentRegistrationBinding.inflate(layoutInflater)
    private var currentUserType = UserType.LEGAL

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
        }
    }

    override fun setupUI() {
    }

    override fun handleState(state: SimpleStates) {

    }
}
