package com.example.noteslist.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.example.noteslist.R
import com.example.noteslist.domain.Note
import kotlin.math.max
import kotlin.math.min
import androidx.core.view.isGone

class NoteStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0

) : ViewGroup(context, attrs, defStyleAttr) {
    private val isExpanded = false

    // Значения по умолчанию
    private var defaultStackSpacing = 0f
    private var defaultStackMaxVisible = 6

    // Стейт
    private var stackSpacing = defaultStackSpacing
    private var stackMaxVisible = defaultStackMaxVisible
    private var stackVerticalSpacing = 0f

    init {
        initDimensions()
    }

    private fun initDimensions() {
        val resources = context.resources
        resources.apply {
            defaultStackSpacing = getDimension(R.dimen.default_stack_spacing)
            stackSpacing = defaultStackSpacing
            stackMaxVisible = defaultStackMaxVisible
            stackVerticalSpacing = getDimension(R.dimen.stack_vertical_spacing)
        }
    }

    override fun shouldDelayChildPressedState(): Boolean = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
       var totalWidth = 0f
       var maxWidth = 0f
       val notesCount = minOf(stackMaxVisible, childCount)
       var totalHeight = 0f

       var prevChildHeight = stackSpacing
       var prevChildWidth = stackSpacing
       for (i in 0 until notesCount) {
           val child = getChildAt(i)
           if (child.isGone) continue

           measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)

           val lp = child.layoutParams as MarginLayoutParams
           val childTotalWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
           val childTotalHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

           if (isExpanded) {
               totalHeight += childTotalHeight
               maxWidth = maxOf(maxWidth, childTotalWidth.toFloat())
           } else {
               totalHeight += stackSpacing + childTotalHeight - prevChildHeight
               totalWidth += stackSpacing + childTotalWidth - prevChildWidth
           }

           prevChildHeight = childTotalHeight.toFloat()
           prevChildWidth = childTotalWidth.toFloat()
       }
       if (isExpanded) {
           totalHeight += stackVerticalSpacing * (childCount - 1)
           totalWidth = maxWidth
       }

       val measuredWidth = resolveSize(totalWidth.toInt(), widthMeasureSpec)
       val measuredHeight = resolveSize(totalHeight.toInt(), heightMeasureSpec)
       setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(
        changed: Boolean,
        parentLeft: Int,
        parentTop: Int,
        parentRight: Int,
        parentBottom: Int
    ) {
       val notesCount = minOf(stackMaxVisible,  childCount)

       if (isExpanded) {
           val left = paddingLeft
           var top = paddingTop
           for (i in 0 until notesCount) {
               val child = getChildAt(i)
               if (child.isGone) continue

               val lp = child.layoutParams as MarginLayoutParams
               val childTotalWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
               val childTotalHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

               val right = left + childTotalWidth
               val bottom = top + childTotalHeight
               child.layout(left, top, right, bottom)
               top += childTotalHeight + stackVerticalSpacing.toInt()
           }
       } else {
           val stackSpacingInt = stackSpacing.toInt()
           for (i in 0 until notesCount) {
               val child = getChildAt(i)
               if (child.isGone) continue

               val lp = child.layoutParams as MarginLayoutParams
               val childTotalWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
               val childTotalHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

               val left = paddingLeft + i * stackSpacingInt
               val top = paddingTop + i * stackSpacingInt
               val right = left + childTotalWidth
               val bottom = top + childTotalHeight
               child.layout(left, top, right, bottom)
           }
       }
    }

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