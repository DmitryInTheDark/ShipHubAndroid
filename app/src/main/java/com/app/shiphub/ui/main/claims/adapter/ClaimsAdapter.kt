package com.app.shiphub.ui.main.claims.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.base.BaseAdapter
import com.app.base.BaseViewHolder
import com.app.data.models.enums.ClaimStatus
import com.app.shiphub.databinding.HolderClaimBinding
import com.app.shiphub.databinding.HolderClaimsHeaderBinding

class ClaimsAdapter(
    private val callback: ClaimsAdapterCallback
): BaseAdapter<ClaimHolders, BaseViewHolder>(
    ClaimDiffUtilCallback()
) {

    interface ClaimsAdapterCallback{
        fun onStatusChange(status: ClaimStatus?)
        fun onScroll(scrollX: Int)
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is ClaimHolders.ClaimsHeaderHolderModel -> ClaimsHolderTypes.HEADER.ordinal
            is ClaimHolders.ClaimsHolderModel -> ClaimsHolderTypes.CLAIM.ordinal
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ClaimsHolderTypes.HEADER.ordinal -> ClaimsHeaderViewHolder(HolderClaimsHeaderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false), callback)
            ClaimsHolderTypes.CLAIM.ordinal -> ClaimViewHolder(HolderClaimBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(
        viewHolder: BaseViewHolder,
        position: Int
    ) {
        when(viewHolder){
            is ClaimsHeaderViewHolder -> viewHolder.bind(
                getItem(position) as ClaimHolders.ClaimsHeaderHolderModel
            )
            is ClaimViewHolder -> viewHolder.bind(
                getItem(position) as ClaimHolders.ClaimsHolderModel
            )
        }
    }

    private enum class ClaimsHolderTypes{
        HEADER, CLAIM
    }
}