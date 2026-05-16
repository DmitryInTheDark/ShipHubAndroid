package com.app.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
    val context = binding.root.context!!
}