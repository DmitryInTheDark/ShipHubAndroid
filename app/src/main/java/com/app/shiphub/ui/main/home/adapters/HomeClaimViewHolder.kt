package com.app.shiphub.ui.main.home.adapters

import androidx.core.view.isVisible
import com.app.base.BaseViewHolder
import com.app.shiphub.R
import com.app.shiphub.databinding.HomeClaimHolderBinding
import timber.log.Timber

class HomeClaimViewHolder(
    private val binding: HomeClaimHolderBinding,
    private val callback: HomeClaimsAdapter.HomeClaimCallback
): BaseViewHolder(binding) {

    fun bind(holder: HomeClaimHolderModel) = with(binding){
        tvClaimNumber.text = context.getString(R.string.number_of_claim, holder.id)
        tvServiceType.text = context.getString(R.string.service_type, holder.testType)
        tvStatus.text = context.getString(R.string.status_type, holder.status)
        if (holder.lastUpdate.isBlank()) Timber.i("yes")
        else Timber.i(holder.lastUpdate)
        tvLastUpdate.apply {
            isVisible = holder.lastUpdate.isNotBlank()
            text = context.getString(R.string.last_update, holder.lastUpdate)
        }
        root.setOnClickListener {
            callback.onClick(holder.id)
        }
    }

}
