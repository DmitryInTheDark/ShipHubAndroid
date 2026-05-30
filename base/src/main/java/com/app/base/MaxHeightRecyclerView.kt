package com.app.base

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.withStyledAttributes

class MaxHeightRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var maxHeight = Int.MAX_VALUE

    init {
        attrs?.let {
            context.withStyledAttributes(
                it,
                R.styleable.MaxHeightRecyclerView
            ) {

                maxHeight = getDimensionPixelSize(
                    R.styleable.MaxHeightRecyclerView_maxHeight,
                    Int.MAX_VALUE
                )

            }
        }
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {

        val newHeightSpec = MeasureSpec.makeMeasureSpec(
            maxHeight,
            MeasureSpec.AT_MOST
        )

        super.onMeasure(widthSpec, newHeightSpec)
    }
}