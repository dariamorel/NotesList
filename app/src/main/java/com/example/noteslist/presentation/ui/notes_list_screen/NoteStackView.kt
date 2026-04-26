package com.example.noteslist.presentation.ui.notes_list_screen

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.PathInterpolator
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.core.view.isGone
import androidx.navigation.findNavController
import com.example.noteslist.R
import com.example.noteslist.domain.Note
import com.google.android.material.button.MaterialButton
import kotlin.math.min

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
    private val defaultCornerRadius = context.resources.getDimension(R.dimen.default_corner_radius)

    // Стейт
    private var stackSpacing = defaultStackSpacing
    private var stackMaxVisible = defaultStackMaxVisible
    private var stackVerticalSpacing = 0f
    private var backButtonSize = 0f
    private var isExpandAnimating = false
    private var pendingExpandAnimation = false
    private val stackInterpolator = PathInterpolator(0.4f, 0.1f, 0.2f, 1f)

    // Callback
    private var callback: OnChangeListener? = null

    private var onClick: (Note) -> Unit = {}
    private val showBackButtonRunnable = Runnable {
        if (!isExpanded) return@Runnable
        showBackButtonAnimated()
    }

    // View
    private lateinit var backButton: MaterialButton

    init {
        isChildrenDrawingOrderEnabled = true
        initDimensions()
        initAttrs(attrs, defStyleAttr, defStyleRes)
        initButton()
        initClicks()
    }

    fun submitNotes(notes: List<Note>) {
        cancelAnimations()
        removeAllViews()

        notes
            .sortedBy { it.createTime }
            .forEach { note ->
                val noteView = NoteView(context)
                noteView.setNote(note)

                noteView.setOnClickListener {
                    onClick(note)
                }

                noteView.setOnLongClickListener {
                    callback?.onReadChanged(
                        note = note,
                        isRead = !note.isRead
                    )
                    true
                }

                noteView.setOnChangeListener(object : NoteView.OnChangeListener {
                    override fun onImportanceChanged(isImportant: Boolean) {
                        callback?.onImportanceChanged(
                            note = note,
                            isImportant = isImportant
                        )
                    }
                })

                addView(noteView)
            }

        addView(backButton)

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

    private fun initButton() {
        backButton = LayoutInflater.from(context).inflate(
                R.layout.note_stack_view_btn_back,
                this,
                false
            ) as MaterialButton

        backButton.visibility = GONE
        backButton.alpha = 0f
        backButton.scaleX = BACK_BUTTON_START_SCALE
        backButton.scaleY = BACK_BUTTON_START_SCALE
        backButton.setTextColor(defaultTextColor)

        addView(backButton)
    }

    private fun initClicks() {
        isClickable = true

        setOnClickListener {
            if (!isExpanded && !isExpandAnimating) {
                startExpand()
            }
        }

        backButton.setOnClickListener {
            if (!isExpanded) return@setOnClickListener
            cancelAnimations()
            hideBackButton()
            isExpanded = false
            requestLayout()
        }
    }

    override fun shouldDelayChildPressedState(): Boolean = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
       var totalWidth = 0f
       var maxWidth = 0f
       val visibleNotesCount = minOf(stackMaxVisible, childCount - 1)
       val measuredNotesCount = if (isExpanded) childCount - 1 else visibleNotesCount
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

        // Измеряем кнопку
        val child = getChildAt(childCount - 1)
        measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
        val lp = child.layoutParams as MarginLayoutParams
        val childTotalWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
        val childTotalHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin + defaultCornerRadius * 2

        if (isExpanded) {
           totalHeight += stackVerticalSpacing * (childCount - 2).coerceAtLeast(0)
           totalWidth = maxOf(maxWidth, childTotalWidth.toFloat())
           totalHeight += childTotalHeight
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
       val visibleNotesCount = minOf(stackMaxVisible, childCount - 1)

       if (isExpanded) {
           val left = paddingLeft
           var top = paddingTop
           for (i in 0 until childCount - 1) {
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
           val btn = backButton
           val btnLp = btn.layoutParams as MarginLayoutParams
           val btnLeft = paddingLeft + btnLp.leftMargin
           val btnTop = top + btnLp.topMargin
           val btnRight = btnLeft + btn.measuredWidth
           val btnBottom = btnTop + btn.measuredHeight
           btn.layout(btnLeft, btnTop, btnRight, btnBottom)

           if (pendingExpandAnimation) {
               pendingExpandAnimation = false
               startExpandAnimation(visibleNotesCount)
           }
       } else {
           val stackSpacingInt = stackSpacing.toInt()
           val lastVisibleSlot = (visibleNotesCount - 1).coerceAtLeast(0)
           for (i in 0 until childCount - 1) {
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

           backButton.layout(0, 0, 0, 0)
       }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return (!isExpanded) || isExpandAnimating
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }

    fun setOnChangeListener(callback: OnChangeListener) {
        this.callback = callback
    }

    fun setOnClick(callback: (Note) -> Unit) {
        onClick = callback
    }

    interface OnChangeListener {
        fun onImportanceChanged(note: Note, isImportant: Boolean)

        fun onReadChanged(note: Note, isRead: Boolean)
    }

    override fun getChildDrawingOrder(childCount: Int, drawingPosition: Int): Int {
        val backButtonIndex = childCount - 1
        if (drawingPosition == childCount - 1) return backButtonIndex

        val notesCount = (childCount - 1).coerceAtLeast(0)
        val visibleNotesCount = minOf(stackMaxVisible, notesCount)
        val hiddenCount = (notesCount - visibleNotesCount).coerceAtLeast(0)
        val index = if (drawingPosition < hiddenCount) {
            visibleNotesCount + drawingPosition
        } else {
            visibleNotesCount - 1 - (drawingPosition - hiddenCount)
        }
        return index.coerceIn(0, notesCount - 1)
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
        if (isEmpty() || childCount <= 1) {
            isExpandAnimating = false
            showBackButtonRunnable.run()
            return
        }

        val totalWaveDuration =
            min(MAX_ANIMATION_DURATION, BASE_ANIMATION_DURATION + (childCount - 1) * ANIMATION_STEP)
        val stackSpacingInt = stackSpacing.toInt()
        val lastVisibleSlot = (visibleNotesCount - 1).coerceAtLeast(0)

        for (i in 0 until childCount - 1) {
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

    private fun showBackButtonAnimated() {
        backButton.animate().cancel()
        backButton.visibility = VISIBLE
        backButton.alpha = 0f
        backButton.scaleX = BACK_BUTTON_START_SCALE
        backButton.scaleY = BACK_BUTTON_START_SCALE
        backButton.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(BACK_BUTTON_ANIMATION_DURATION.toLong())
            .setStartDelay(0L)
            .setInterpolator(stackInterpolator)
            .withEndAction { isExpandAnimating = false }
            .start()
    }

    private fun cancelAnimations() {
        isExpandAnimating = false
        pendingExpandAnimation = false
        backButton.animate().cancel()
        for (i in 0 until childCount - 1) {
            getChildAt(i).animate().cancel()
            getChildAt(i).translationY = 0f
        }
    }

    private fun hideBackButton() {
        backButton.visibility = GONE
        backButton.alpha = 0f
        backButton.scaleX = BACK_BUTTON_START_SCALE
        backButton.scaleY = BACK_BUTTON_START_SCALE
    }

    private fun startExpand() {
        cancelAnimations()
        hideBackButton()
        isExpanded = true
        isExpandAnimating = true
        pendingExpandAnimation = true
        requestLayout()
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