package com.app.shiphub.ui.main.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.base.BaseAdapter
import com.app.base.BaseDiffUtilCallback
import com.app.shiphub.databinding.HomeClaimHolderBinding

class HomeClaimsAdapter(
    private val callback: HomeClaimCallback
): BaseAdapter<HomeClaimHolderModel, HomeClaimViewHolder>(
    BaseDiffUtilCallback()
) {

    fun interface HomeClaimCallback {
        fun onClick(claimId: Long)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): HomeClaimViewHolder {
        return HomeClaimViewHolder(
            HomeClaimHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            callback
        )
    }

    override fun onBindViewHolder(
        viewHolder: HomeClaimViewHolder,
        position: Int
    ) {
        viewHolder.bind(getItem(position))
    }
}
