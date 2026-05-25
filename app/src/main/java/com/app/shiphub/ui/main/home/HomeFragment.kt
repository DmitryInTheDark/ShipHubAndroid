package com.app.shiphub.ui.main.home

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.base.BaseAdapter
import com.app.base.BasePagingFragment
import com.app.data.models.domain.Claim
import com.app.shiphub.R
import com.app.shiphub.databinding.FragmentHomeBinding
import com.app.shiphub.databinding.HolderHomeNotificationBinding
import com.app.shiphub.ui.main.home.adapters.HomeClaimHolderModel
import com.app.shiphub.ui.main.home.adapters.HomeClaimViewHolder
import com.app.shiphub.ui.main.home.adapters.HomeClaimsAdapter
import com.app.shiphub.util.HorizontalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BasePagingFragment<FragmentHomeBinding, Claim, HomeClaimHolderModel, HomeClaimViewHolder, HomeUIState, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun initializeBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun initAdapterAndRecyclerView(): Pair<BaseAdapter<HomeClaimHolderModel, HomeClaimViewHolder>, RecyclerView> =
        HomeClaimsAdapter { claimId ->
            navigate(HomeFragmentDirections.actionHomeFragmentToClaimFragment2(claimId))
        } to binding.rvClaims

    override fun setLightLoading(isLoading: Boolean) {
        binding.srlHome.isRefreshing = isLoading
    }

    override fun setupListeners() {
        with(binding){
            btnCreateClaimFirst.setOnClickListener {
                navigate(R.id.createClaimFragment)
            }
            btnCreateClaimSecond.setOnClickListener {
                navigate(R.id.createClaimFragment)
            }
            srlHome.setOnRefreshListener{
                viewModel.loadFirstPage()
            }
            btnMyRequests.setOnClickListener {
                navigate(HomeFragmentDirections.actionHomeFragmentToGraphClaims())
            }
            btnToDocuments.setOnClickListener {
                navigate(HomeFragmentDirections.actionHomeFragmentToDocumentsFragment())
            }
        }
    }

    override fun setupUI() {
        with(binding){
            rvClaims.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(HorizontalSpaceItemDecoration(16.dpToPx().toInt()))
            }
        }
    }

    override fun setLoadingState(isLoading: Boolean) {
        super.setLoadingState(isLoading)
        binding.srlHome.isRefreshing = isLoading
    }

    override fun showEmptyPlaceholder() = with(binding){
        rvClaims.isVisible = false
        cvCreateClaim.isVisible = true
        btnCreateClaimSecond.isVisible = true
        tvCreateClaim.text = getString(R.string.create_your_first_claim)
    }

    override fun setupList(items: List<HomeClaimHolderModel>) = with(binding){
        cvCreateClaim.isVisible = false
        btnCreateClaimSecond.isVisible = false
        rvClaims.isVisible = true
        super.setupList(items)
    }

    override fun setErrorPlaceholder(error: String) = with(binding){
        rvClaims.isVisible = false
        cvCreateClaim.isVisible = true
        btnCreateClaimSecond.isVisible = false
        tvCreateClaim.text = getString(R.string.claims_get_error)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun handleState(state: HomeUIState) {
        with(binding){
            when(state){
                is HomeUIState.InitUserInfo -> {
                    tvWelcome.text = getString(R.string.welcome, state.user.username)
                    llNotifications.removeAllViews()
                    if (state.notifications.isEmpty()) {
                        val notifBinding = HolderHomeNotificationBinding.inflate(layoutInflater).apply {
                            tvNotifText.text = getString(R.string.notifications_is_empty)
                        }
                        llNotifications.addView(notifBinding.root)
                    }
                }
                is HomeUIState.ShowNotifications -> {
                    llNotifications.removeAllViews()
                    if (state.notifications.isEmpty()) {
                        val notifBinding = HolderHomeNotificationBinding.inflate(layoutInflater).apply {
                            tvNotifText.text = getString(R.string.notifications_is_empty)
                        }
                        llNotifications.addView(notifBinding.root)
                    }
                }
            }
        }
    }
}
