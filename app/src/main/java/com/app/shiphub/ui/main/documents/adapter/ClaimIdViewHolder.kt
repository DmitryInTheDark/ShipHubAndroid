package com.app.shiphub.ui.main.documents.adapter

import com.app.base.BaseViewHolder
import com.app.shiphub.R
import com.app.shiphub.databinding.HolderTextBinding

class ClaimIdViewHolder(
    private val binding: HolderTextBinding,
    private val callback: ClaimIdsAdapter.ClaimIdsCallback
): BaseViewHolder(binding) {

    fun bind(holder: ClaimIdHolderModel) = with(binding){
        tvClaimId.apply {
            text = context.getString(R.string.number, holder.id)
            setOnClickListener { callback.onClick(holder.id) }
        }
    }

}