package com.example.noteslist.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.noteslist.R
import com.example.noteslist.domain.Note
import kotlin.math.max
import kotlin.math.min
import androidx.core.view.isGone
import com.example.noteslist.presentation.NoteView.OnChangeListener
import kotlin.Int

class NoteStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private var isExpanded = false

    // Значения по умолчанию
    private var defaultStackSpacing = 0f
    private var defaultStackMaxVisible = 6
    private var defaultTextColor = ContextCompat.getColor(context, R.color.default_text_color)

    // Стейт
    private var stackSpacing = defaultStackSpacing
    private var stackMaxVisible = defaultStackMaxVisible
    private var stackVerticalSpacing = 0f
    private var backButtonSize = 0f

    // Геометрия
    private val backButtonRect = RectF()
    private val backButtonPaint = TextPaint().apply { isAntiAlias = true }

    // Callback
    private var callback: OnChangeListener? = null

    init {
        setWillNotDraw(false)
        initDimensions()
        initAttrs(attrs, defStyleAttr, defStyleRes)
        initPaints()
    }

    private fun initDimensions() {
        val resources = context.resources
        resources.apply {
            defaultStackSpacing = getDimension(R.dimen.default_stack_spacing)
            stackSpacing = defaultStackSpacing
            stackMaxVisible = defaultStackMaxVisible
            stackVerticalSpacing = getDimension(R.dimen.stack_vertical_spacing)
            backButtonSize = getDimension(R.dimen.back_button_size)
        }
    }

    private fun initPaints() {
        backButtonPaint.apply {
            textSize = backButtonSize
            typeface = Typeface.DEFAULT
            color = defaultTextColor
        }
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NoteStackView, defStyleAttr, defStyleRes)
            try {
                stackSpacing = typedArray.getDimension(R.styleable.NoteStackView_stackSpacing, defaultStackSpacing)
                stackMaxVisible = typedArray.getInteger(R.styleable.NoteStackView_stackMaxVisible, defaultStackMaxVisible)
            } finally {
                typedArray.recycle()
            }
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
           totalHeight += stackVerticalSpacing * childCount
           totalWidth = maxWidth
           totalHeight += backButtonSize
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

           backButtonRect.set(
               paddingLeft.toFloat(),
               top.toFloat(),
               (width - paddingRight).toFloat(),
               top + backButtonSize
           )
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isExpanded) {
            canvas.drawText("<< Свернуть", backButtonRect.left, backButtonRect.centerY(), backButtonPaint)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return !isExpanded
    }   

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isExpanded && backButtonRect.contains(touchX, touchY)) {
                    isExpanded = false
                    callback?.onExpandedChanged(false)
                    requestLayout()
                    invalidate()
                    return true
                }
                if (!isExpanded) {
                    isExpanded = true
                    callback?.onExpandedChanged(true)
                    requestLayout()
                    invalidate()
                    return true
                }
            }
        }

        return super.onTouchEvent(event)
    }

    fun setOnChangeListener(callback: OnChangeListener) {
        this.callback = callback
    }

    interface OnChangeListener {
        fun onExpandedChanged(isExpanded: Boolean)
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