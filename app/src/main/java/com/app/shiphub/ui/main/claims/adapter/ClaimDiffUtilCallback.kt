package com.app.shiphub.ui.main.claims.adapter

import com.app.base.BaseDiffUtilCallback

class ClaimDiffUtilCallback: BaseDiffUtilCallback<ClaimHolders>() {

    override fun areItemsTheSame(p0: ClaimHolders, p1: ClaimHolders): Boolean {
        if (p0 is ClaimHolders.ClaimsHolderModel && p1 is ClaimHolders.ClaimsHolderModel) {
            return p0.id == p1.id
        }
        return p0::class.java == p1::class.java
    }

}