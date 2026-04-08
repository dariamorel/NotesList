package com.example.noteslist.presentation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.PathInterpolator
import androidx.core.content.ContextCompat
import com.example.noteslist.R
import com.example.noteslist.domain.Note
import kotlin.math.min
import androidx.core.view.isGone
import androidx.core.view.isEmpty

class NoteStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private var isExpanded = false

    // Значения по умолчанию
    private var defaultStackSpacing = 0f
    private var defaultStackMaxVisible = 3
    private var defaultTextColor = ContextCompat.getColor(context, R.color.default_text_color)

    // Стейт
    private var stackSpacing = defaultStackSpacing
    private var stackMaxVisible = defaultStackMaxVisible
    private var stackVerticalSpacing = 0f
    private var backButtonSize = 0f
    private var isExpandAnimating = false
    private var pendingExpandAnimation = false
    private var backButtonAlpha = 0f
    private var backButtonScale = BACK_BUTTON_START_SCALE

    // Геометрия
    private val backButtonRect = RectF()
    private val backButtonPaint = TextPaint().apply { isAntiAlias = true }
    private val stackInterpolator = PathInterpolator(0.4f, 0.1f, 0.2f, 1f)
    private var backButtonAnimator: ValueAnimator? = null

    // Callback
    private var callback: OnChangeListener? = null
    private val showBackButtonRunnable = Runnable {
        if (!isExpanded) return@Runnable
        animateBackButtonIn()
    }

    init {
        setWillNotDraw(false)
        setChildrenDrawingOrderEnabled(true)
        initDimensions()
        initAttrs(attrs, defStyleAttr, defStyleRes)
        initPaints()
    }

    fun submitNotes(notes: List<Note>) {
        resetAnimations()
        removeAllViews()

        notes
            .sortedBy { it.createTime }
            .forEach { note ->
                val noteView = NoteView(context)
                noteView.setNote(note)

                noteView.setOnChangeListener(object : NoteView.OnChangeListener {
                    override fun onImportanceChanged(isImportant: Boolean) {
                    }

                    override fun onReadChanged(isRead: Boolean) {
                    }
                })

                addView(noteView)
            }

        requestLayout()
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
       val visibleNotesCount = minOf(stackMaxVisible, childCount)
       val measuredNotesCount = if (isExpanded) childCount else visibleNotesCount
       var totalHeight = 0f

       var prevChildHeight = stackSpacing
       var prevChildWidth = stackSpacing
       for (i in 0 until measuredNotesCount) {
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
           totalHeight += stackVerticalSpacing * (childCount - 1).coerceAtLeast(0)
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
       val visibleNotesCount = minOf(stackMaxVisible, childCount)

       if (isExpanded) {
           val left = paddingLeft
           var top = paddingTop
           for (i in 0 until childCount) {
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

           if (pendingExpandAnimation) {
               pendingExpandAnimation = false
               startExpandAnimation(visibleNotesCount)
           }
       } else {
           val stackSpacingInt = stackSpacing.toInt()
           val lastVisibleSlot = (visibleNotesCount - 1).coerceAtLeast(0)
           for (i in 0 until childCount) {
               val child = getChildAt(i)
               if (child.isGone) continue

               val lp = child.layoutParams as MarginLayoutParams
               val childTotalWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
               val childTotalHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

               val slot = min(i, lastVisibleSlot)
               val left = paddingLeft + slot * stackSpacingInt
               val top = paddingTop + slot * stackSpacingInt
               val right = left + childTotalWidth
               val bottom = top + childTotalHeight
               child.layout(left, top, right, bottom)
               child.translationY = 0f
           }
       }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isExpanded && backButtonAlpha > 0f) {
            val text = "<< Свернуть"
            val originalAlpha = backButtonPaint.alpha
            val textWidth = backButtonPaint.measureText(text)
            val centerX = backButtonRect.centerX()
            val centerY = backButtonRect.centerY()
            val fontMetrics = backButtonPaint.fontMetrics
            val baseline = -((fontMetrics.ascent + fontMetrics.descent) / 2f)

            backButtonPaint.alpha = (255 * backButtonAlpha).toInt()
            canvas.save()
            canvas.translate(centerX, centerY)
            canvas.scale(backButtonScale, backButtonScale)
            canvas.drawText(text, -textWidth / 2f, baseline, backButtonPaint)
            canvas.restore()
            backButtonPaint.alpha = originalAlpha
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return !isExpanded || isExpandAnimating
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isExpanded && backButtonRect.contains(touchX, touchY)) {
                    resetAnimations()
                    isExpanded = false
                    callback?.onExpandedChanged(false)
                    requestLayout()
                    invalidate()
                    return true
                }
                if (!isExpanded && !isExpandAnimating) {
                    backButtonAnimator?.cancel()
                    backButtonAlpha = 0f
                    backButtonScale = BACK_BUTTON_START_SCALE
                    isExpanded = true
                    isExpandAnimating = true
                    pendingExpandAnimation = true
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

    override fun getChildDrawingOrder(childCount: Int, drawingPosition: Int): Int {
        if (isExpanded && !isExpandAnimating) {
            return drawingPosition
        }

        val visibleNotesCount = minOf(stackMaxVisible, childCount)
        val hiddenCount = (childCount - visibleNotesCount).coerceAtLeast(0)
        return if (drawingPosition < hiddenCount) {
            visibleNotesCount + drawingPosition
        } else {
            visibleNotesCount - 1 - (drawingPosition - hiddenCount)
        }.coerceIn(0, childCount - 1)
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

    private fun startExpandAnimation(visibleNotesCount: Int) {
        if (isEmpty()) {
            isExpandAnimating = false
            showBackButtonRunnable.run()
            return
        }

        val totalWaveDuration =
            min(MAX_ANIMATION_DURATION, BASE_ANIMATION_DURATION + childCount * ANIMATION_STEP)
        val stackSpacingInt = stackSpacing.toInt()
        val lastVisibleSlot = (visibleNotesCount - 1).coerceAtLeast(0)

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            // позиция в свернутом состоянии
            val collapsedTop = paddingTop + min(i, lastVisibleSlot) * stackSpacingInt
            val startTranslationY = (collapsedTop - child.top).toFloat()
            val startDelay = i * CHILD_START_DELAY
            val childDuration = (totalWaveDuration - startDelay).coerceAtLeast(MIN_CHILD_ANIMATION_DURATION)

            child.animate().cancel()
            child.translationY = startTranslationY
            child.animate()
                .translationY(0f)
                .setStartDelay(startDelay.toLong())
                .setDuration(childDuration.toLong())
                .setInterpolator(stackInterpolator)
                .start()
        }

        postDelayed(showBackButtonRunnable, (totalWaveDuration + BACK_BUTTON_DELAY).toLong())
    }

    private fun animateBackButtonIn() {
        backButtonAnimator?.cancel()
        backButtonAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = BACK_BUTTON_ANIMATION_DURATION.toLong()
            interpolator = stackInterpolator
            addUpdateListener { animator ->
                val progress = animator.animatedValue as Float
                backButtonAlpha = progress
                backButtonScale =
                    BACK_BUTTON_START_SCALE + (1f - BACK_BUTTON_START_SCALE) * progress
                invalidate()
            }
            start()
        }
        isExpandAnimating = false
    }

    private fun resetAnimations() {
        isExpandAnimating = false
        pendingExpandAnimation = false
        backButtonAnimator?.cancel()
        backButtonAlpha = 0f
        backButtonScale = BACK_BUTTON_START_SCALE
        for (i in 0 until childCount) {
            getChildAt(i).animate().cancel()
            getChildAt(i).translationY = 0f
        }
    }

    private companion object {
        const val CHILD_START_DELAY = 20
        const val BASE_ANIMATION_DURATION = 200
        const val ANIMATION_STEP = 40
        const val MAX_ANIMATION_DURATION = 800
        const val MIN_CHILD_ANIMATION_DURATION = 1
        const val BACK_BUTTON_DELAY = 100
        const val BACK_BUTTON_ANIMATION_DURATION = 200
        const val BACK_BUTTON_START_SCALE = 0.7f
    }
}