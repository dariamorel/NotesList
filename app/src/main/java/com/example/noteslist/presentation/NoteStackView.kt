package com.example.noteslist.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class NoteStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0

) : ViewGroup(context, attrs, defStyleAttr) {
    override fun shouldDelayChildPressedState(): Boolean = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {}

    override fun onLayout(
        changed: Boolean,
        parentLeft: Int,
        parentTop: Int,
        parentRight: Int,
        parentBottom: Int
    ) {}

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun checkLayoutParams(lp: LayoutParams?): Boolean {
        return lp is MarginLayoutParams
    }
}