package com.app.shiphub.ui.main.claims.adapter

import androidx.core.view.isVisible
import com.app.base.BaseViewHolder
import com.app.shiphub.R
import com.app.shiphub.databinding.HolderClaimBinding

class ClaimViewHolder(
    private val binding: HolderClaimBinding
): BaseViewHolder(binding) {

    fun bind(holder: ClaimHolders.ClaimsHolderModel) = with(binding){
        tvClaimNumber.text = context.getString(R.string.claim_number, holder.id)
        tvTestType.text = context.getString(R.string.service_type, holder.testType)
        tvStatus.text = context.getString(R.string.status_type, holder.status)
        tvEquipmentName.text = context.getString(R.string.equipment_name, holder.equipment)
        tvLastUpdate.apply {
            isVisible = holder.lastUpdate.isNotBlank()
            text = context.getString(R.string.last_update, holder.lastUpdate)
        }
    }

}