package com.app.base

import androidx.recyclerview.widget.ListAdapter

abstract class BaseAdapter<M: BaseHolderModel, VH: BaseViewHolder>(
    diffUtilCallback: BaseDiffUtilCallback<M>
): ListAdapter<M, VH>(diffUtilCallback)