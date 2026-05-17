package com.app.shiphub.ui.main.claims

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.app.base.BaseAdapter
import com.app.base.BasePagingFragment
import com.app.data.models.domain.Claim
import com.app.data.models.enums.ClaimStatus
import com.app.shiphub.R
import com.app.shiphub.databinding.FragmentClaimsBinding
import com.app.shiphub.ui.main.claims.adapter.ClaimViewHolder
import com.app.shiphub.ui.main.claims.adapter.ClaimsAdapter
import com.app.shiphub.ui.main.claims.adapter.ClaimsHolderModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimsFragment: BasePagingFragment<Claim, ClaimsHolderModel, ClaimViewHolder, ClaimsUIState, ClaimsViewModel, FragmentClaimsBinding>() {

    override fun initAdapterAndRecyclerView()
    : Pair<BaseAdapter<ClaimsHolderModel, ClaimViewHolder>, RecyclerView> = ClaimsAdapter() to binding.rvClaims

    override fun setLightLoading(isLoading: Boolean) {
        binding.srlClaims.isRefreshing = isLoading
    }

    override fun showEmptyPlaceholder() {

    }

    override fun setErrorPlaceholder(error: String) {

    }

    override val viewModel: ClaimsViewModel by viewModels()

    override fun initializeBinding() = FragmentClaimsBinding.inflate(layoutInflater)

    override fun setupListeners() = with(binding){
        cgStatuses.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()){
                viewModel.loadFirstPage()
            }else{
                viewModel.filterClaimsByStatus(ClaimStatus.entries[checkedIds.first()-1])
            }
        }
    }

    override fun setupUI() = with(binding){
//        tlTypes.removeAllTabs()
//        ClaimStatus.entries.forEach { status ->
//            val tab = tlTypes.newTab().setText(status.displayName)
//            tlTypes.addTab(tab)
//        }
//        val tabStrip = tlTypes.getChildAt(0) as ViewGroup
//        for (i in 0 until tabStrip.childCount) {
//            val tabView = tabStrip.getChildAt(i)
//            val params = tabView.layoutParams as ViewGroup.MarginLayoutParams
//            params.marginEnd = 8.dpToPx()
//            tabView.layoutParams = params
//        }
        ClaimStatus.entries.forEach { status ->
            val chip = Chip(root.context).apply {
                text = status.displayName
                chipStrokeWidth = 1.dpToPx().toFloat()
                chipStrokeColor = ContextCompat.getColorStateList(
                    requireContext(), R.color.chip_stroke_color_selector
                )
                isCheckable = true
                isClickable = true
                chipBackgroundColor = ContextCompat.getColorStateList(
                    requireContext(), R.color.chip_background_selector
                )
                setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(), R.color.chip_text_selector
                    )
                )

            }
            cgStatuses.addView(chip)
        }
    }

    override fun handleState(state: ClaimsUIState) {

    }
}