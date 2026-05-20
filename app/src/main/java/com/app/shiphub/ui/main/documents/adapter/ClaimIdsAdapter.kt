package com.app.shiphub.ui.main.documents.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.base.BaseAdapter
import com.app.base.BaseDiffUtilCallback
import com.app.shiphub.databinding.HolderTextBinding

class ClaimIdsAdapter(
    private val callback: ClaimIdsCallback
): BaseAdapter<ClaimIdHolderModel, ClaimIdViewHolder>(
    BaseDiffUtilCallback(),
) {

    fun interface ClaimIdsCallback{
        fun onClick(claimId: Long)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClaimIdViewHolder = ClaimIdViewHolder(HolderTextBinding.inflate(LayoutInflater.from(parent.context), parent, false), callback)

    override fun onBindViewHolder(
        viewHolder: ClaimIdViewHolder,
        position: Int
    ){
        viewHolder.bind(getItem(position))
    }

}