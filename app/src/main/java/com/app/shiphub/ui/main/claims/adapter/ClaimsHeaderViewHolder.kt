package com.app.shiphub.ui.main.claims.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import com.app.base.BaseViewHolder
import com.app.data.models.enums.ClaimStatus
import com.app.shiphub.R
import com.app.shiphub.databinding.HolderClaimsHeaderBinding
import com.app.shiphub.util.dpToPx
import com.google.android.material.chip.Chip

class ClaimsHeaderViewHolder(
    private val binding: HolderClaimsHeaderBinding,
    private val callback: ClaimsAdapter.ClaimsAdapterCallback
): BaseViewHolder(binding) {

    fun bind(holder: ClaimHolders.ClaimsHeaderHolderModel) = with(binding) {
        cgStatuses.setOnCheckedStateChangeListener(null)
        if (cgStatuses.isEmpty()) {
            ClaimStatus.entries.forEachIndexed { index, status ->
                val chip = Chip(root.context).apply {
                    id = index
                    text = status.displayName
                    chipStrokeWidth = 1.dpToPx(context)
                    chipStrokeColor = ContextCompat.getColorStateList(
                        context, R.color.chip_stroke_color_selector
                    )
                    isCheckable = true
                    isClickable = true
                    chipBackgroundColor = ContextCompat.getColorStateList(
                        context, R.color.chip_background_selector
                    )
                    setTextColor(
                        ContextCompat.getColorStateList(
                            context, R.color.chip_text_selector
                        )
                    )
                    tag = status
                }
                cgStatuses.addView(chip)
            }
        }

        val checkedChipId = (0 until cgStatuses.childCount)
            .map { cgStatuses.getChildAt(it) as Chip }
            .find { it.tag == holder.checkedStatus }
            ?.id ?: View.NO_ID

        if (cgStatuses.checkedChipId != checkedChipId) {
            if (checkedChipId == View.NO_ID) {
                cgStatuses.clearCheck()
            } else {
                cgStatuses.check(checkedChipId)
            }
        }

        cgStatuses.setOnCheckedStateChangeListener { cg, checkedIds ->
            if (checkedIds.isEmpty()) {
                callback.onStatusChange(null)
            } else {
                val status = cg.findViewById<Chip>(checkedIds.first()).tag as ClaimStatus
                callback.onStatusChange(status)
            }
        }

        holder.scrollX?.let {
            hsvChips.post { hsvChips.scrollTo(it, 0) }
        }
        hsvChips.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            callback.onScroll(scrollX)
        }
    }

}