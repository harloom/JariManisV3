package com.app.jarimanis.utils

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

class TopSpacingRecyleViewDecoration constructor(val padding : Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.top = padding
    }
}