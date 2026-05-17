package com.app.shiphub.ui.main.claims.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.base.BaseAdapter
import com.app.base.BaseDiffUtilCallback
import com.app.shiphub.databinding.ClaimHolderBinding

class ClaimsAdapter(): BaseAdapter<ClaimsHolderModel, ClaimViewHolder>(
    BaseDiffUtilCallback<ClaimsHolderModel>()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): ClaimViewHolder {
        return ClaimViewHolder(
            ClaimHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        viewHolder: ClaimViewHolder,
        position: Int
    ) {
        viewHolder.bind(currentList[position])
    }
}