package com.app.base

import androidx.recyclerview.widget.DiffUtil

open class BaseDiffUtilCallback<M : BaseHolderModel>: DiffUtil.ItemCallback<M>() {
    open override fun areItemsTheSame(p0: M, p1: M): Boolean{
        return p0::class == p1::class
    }

    open override fun areContentsTheSame(p0: M, p1: M): Boolean{
        return p0 == p1
    }
}