package com.example.noteslist.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.example.noteslist.R
import androidx.core.graphics.toColorInt
import com.example.noteslist.domain.Note
import kotlin.apply

class NoteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var note: Note? = null

    fun setNote(note: Note) {
        this.note = note
    }

    // Значения по умолчанию
    private val defaultCornerRadius = 16f
    private val defaultBackgroundColor = "#edf1f5".toColorInt()
    private val titleRectColor = "#62abf0".toColorInt()
    private val textHorizontalPadding = 8f
    private val textVerticalPadding = 16f
    private val titleTextSize = 30f
    private val starSize = 40f
    private val starColor = "#ebc12a".toColorInt()

    // Геометрия
    private val frameRect = RectF()
    private val titleFrameRect = RectF()
    private val titleFrameStroke = RectF()
    private val starRect = RectF()

    // Стейт
    private var cornerRadius = defaultCornerRadius
    private var backgroundColor = defaultBackgroundColor

    // Paint
    private val backgroundPaint = Paint().apply { isAntiAlias = true }
    private val titleRectPaint = Paint().apply { isAntiAlias = true }
    private val starPaint = Paint().apply { isAntiAlias = true }
    private val titlePaint = Paint().apply { isAntiAlias = true }

    init {
        initAttrs(attrs, defStyleAttr, defStyleRes)
        initPaints()
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.NoteView, defStyleAttr, defStyleRes)
            try {
                cornerRadius = typedArray.getDimension(R.styleable.NoteView_noteCornerRadius, defaultCornerRadius)
                backgroundColor = typedArray.getColor(R.styleable.NoteView_noteBackgroundColor, defaultBackgroundColor)
            } finally {
                typedArray.recycle()
            }
        }
    }

    private fun initPaints() {
        backgroundPaint.apply {
            style = Paint.Style.FILL
            color = backgroundColor
        }
        titleRectPaint.apply {
            style = Paint.Style.FILL
            color = titleRectColor
        }
        starPaint.apply {
            style = Paint.Style.FILL
            color = starColor
            textSize = starSize
        }
        titlePaint.apply {
            textSize = titleTextSize
            typeface = Typeface.DEFAULT_BOLD
            color = Color.BLACK
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateGeometry()
    }

    private fun updateGeometry() {
        frameRect.set(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (width - paddingRight).toFloat(),
            (height - paddingBottom).toFloat()
        )
        val rectHeight = height - paddingLeft - paddingRight
        val titleRectHeight = rectHeight / 4
        titleFrameRect.set(
            frameRect.left,
            frameRect.top,
            frameRect.right,
            (paddingTop + titleRectHeight).toFloat()
        )
        titleFrameStroke.set(
            titleFrameRect.left,
            titleFrameRect.bottom - cornerRadius,
            titleFrameRect.right,
            titleFrameRect.bottom + cornerRadius
        )
        val starSize = titleFrameStroke.bottom - titleFrameRect.top - textVerticalPadding * 2
        starRect.set(
            titleFrameRect.left + textHorizontalPadding,
            titleFrameRect.top + textVerticalPadding,
            titleFrameRect.left + textHorizontalPadding + starSize,
            titleFrameRect.top + textVerticalPadding + starSize
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        updateGeometry()
        // Фон
        canvas.drawRoundRect(frameRect, cornerRadius, cornerRadius, backgroundPaint)
        canvas.drawRoundRect(titleFrameRect, cornerRadius, cornerRadius, titleRectPaint)
        canvas.drawRect(titleFrameStroke, titleRectPaint)

        // Звезда
        val starSize = starPaint.textSize
        val starX = starRect.centerX() - starSize / 2
        val starBaseline = starRect.centerY() + starSize / 3
        val star = if (note?.isImportant ?: false) "★" else "☆"
        canvas.drawText(star, starX, starBaseline, starPaint)

        // Текст
        val titleX = starRect.right + textHorizontalPadding
        val titleBaseline = starBaseline
        canvas.drawText(note?.title ?: "", titleX, titleBaseline, titlePaint)
    }
}