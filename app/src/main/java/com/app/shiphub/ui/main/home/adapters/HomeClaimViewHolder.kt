package com.app.shiphub.ui.main.home.adapters

import androidx.core.view.isVisible
import com.app.base.BaseViewHolder
import com.app.shiphub.R
import com.app.shiphub.databinding.HomeClaimHolderBinding

class HomeClaimViewHolder(
    private val binding: HomeClaimHolderBinding
): BaseViewHolder(binding) {

    fun bind(holder: HomeClaimHolderModel) = with(binding){
        tvClaimNumber.text = context.getString(R.string.claim_number, holder.id)
        tvServiceType.text = context.getString(R.string.service_type, holder.testType)
        tvStatus.text = context.getString(R.string.status_type, holder.status)
        tvLastUpdate.apply {
            isVisible = holder.lastUpdate.isNotBlank()
            text = context.getString(R.string.last_update, holder.lastUpdate)
        }
    }

}