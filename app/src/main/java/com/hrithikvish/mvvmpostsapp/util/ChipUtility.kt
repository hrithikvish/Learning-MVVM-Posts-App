package com.hrithikvish.mvvmpostsapp.util

import android.content.Context
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ChipUtility {

    companion object {
        fun addChipIntoChipGroup(context: Context, tag: String, chipGroup: ChipGroup) {
            val tagChip = Chip(context).apply {
                this.text = tag
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    chipGroup.removeView(this)
                }
            }
            chipGroup.addView(tagChip)
        }
    }

}