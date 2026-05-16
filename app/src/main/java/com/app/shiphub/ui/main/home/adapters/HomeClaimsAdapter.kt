package com.app.shiphub.ui.main.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.base.BaseAdapter
import com.app.base.BaseDiffUtilCallback
import com.app.shiphub.databinding.HomeClaimHolderBinding

class HomeClaimsAdapter: BaseAdapter<HomeClaimHolderModel, HomeClaimViewHolder>(
    BaseDiffUtilCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): HomeClaimViewHolder {
        return HomeClaimViewHolder(HomeClaimHolderBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(
        viewHolder: HomeClaimViewHolder,
        position: Int
    ) {
        viewHolder.bind(currentList[position])
    }
}