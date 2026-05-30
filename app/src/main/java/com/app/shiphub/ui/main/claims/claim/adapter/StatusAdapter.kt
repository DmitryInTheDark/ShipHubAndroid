package com.app.shiphub.ui.main.claims.claim.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.app.base.BaseAdapter
import com.app.base.BaseDiffUtilCallback
import com.app.base.BaseHolderModel
import com.app.base.BaseViewHolder
import com.app.data.models.enums.ClaimStatus
import com.app.shiphub.databinding.HolderStatusItemBinding

data class StatusHolderModel(
    val status: ClaimStatus,
    val colorRes: Int
) : BaseHolderModel

class StatusAdapter(
    private val onStatusClick: (ClaimStatus) -> Unit
) : BaseAdapter<StatusHolderModel, StatusAdapter.StatusViewHolder>(BaseDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = HolderStatusItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StatusViewHolder(private val binding: HolderStatusItemBinding) : BaseViewHolder(binding) {
        fun bind(item: StatusHolderModel) = with(binding) {
            tvStatusName.text = item.status.displayName
            vStatusIndicator.backgroundTintList = ContextCompat.getColorStateList(context, item.colorRes)
            
            cvRoot.setOnClickListener {
                onStatusClick(item.status)
            }
        }
    }
}
