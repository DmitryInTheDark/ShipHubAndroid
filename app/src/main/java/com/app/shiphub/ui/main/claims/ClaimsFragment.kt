package com.app.shiphub.ui.main.claims

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.app.base.BaseAdapter
import com.app.base.BasePagingFragment
import com.app.base.BaseViewHolder
import com.app.data.models.domain.Claim
import com.app.data.models.enums.ClaimStatus
import com.app.shiphub.databinding.FragmentClaimsBinding
import com.app.shiphub.ui.main.claims.adapter.ClaimHolders
import com.app.shiphub.ui.main.claims.adapter.ClaimsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimsFragment: BasePagingFragment<FragmentClaimsBinding, Claim, ClaimHolders, BaseViewHolder, ClaimsUIState, ClaimsViewModel>(),
ClaimsAdapter.ClaimsAdapterCallback{

    private var scrollX: Int? = null

    override fun initAdapterAndRecyclerView()
    : Pair<BaseAdapter<ClaimHolders, BaseViewHolder>, RecyclerView> = ClaimsAdapter(this) to binding.rvClaims

    override fun setLightLoading(isLoading: Boolean) {
        binding.srlClaims.isRefreshing = isLoading
    }

    override fun showEmptyPlaceholder() {
        setupList(emptyList())
    }

    override fun setErrorPlaceholder(error: String) {

    }

    override val viewModel: ClaimsViewModel by viewModels()

    override fun initializeBinding() = FragmentClaimsBinding.inflate(layoutInflater)

    override fun setupListeners() = with(binding){

    }

    override fun setupList(items: List<ClaimHolders>) {
        val itemsWithHeader = mutableListOf<ClaimHolders>(
            ClaimHolders.ClaimsHeaderHolderModel(viewModel.getCurrentStatus(), scrollX)
        )
        itemsWithHeader.addAll(items)
        adapter.submitList(itemsWithHeader)
    }

    override fun setupUI() = with(binding){

    }

    override fun handleState(state: ClaimsUIState) {

    }

    override fun onStatusChange(status: ClaimStatus?) {
        viewModel.filterClaimsByStatus(status)
        val currentItems = adapter.currentList.filterIsInstance<ClaimHolders.ClaimsHolderModel>()
        setupList(currentItems)
    }

    override fun onScroll(scrollX: Int) {
        this.scrollX = scrollX
    }
}