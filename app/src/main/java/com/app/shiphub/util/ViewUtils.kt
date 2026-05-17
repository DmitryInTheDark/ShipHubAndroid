package com.app.shiphub.util

import android.content.Context

fun Int.dpToPx(context: Context): Float{
    val scale = context.resources.displayMetrics.density
    return this*scale + 0.5f
}