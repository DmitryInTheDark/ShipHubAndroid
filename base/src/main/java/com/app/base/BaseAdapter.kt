package com.app.base

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

abstract class BaseAdapter<M: BaseHolderModel, VH: BaseViewHolder>(
    diffUtilCallback: BaseDiffUtilCallback<M>
): ListAdapter<M, VH>(diffUtilCallback) {

    abstract override fun onCreateViewHolder(parent: ViewGroup, position: Int): VH

    abstract override fun onBindViewHolder(viewHolder: VH, position: Int)

}