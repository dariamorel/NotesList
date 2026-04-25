package com.example.noteslist.presentation.ui.notes_list_screen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import com.example.noteslist.R
import com.example.noteslist.domain.Note

class NoteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var note: Note? = null

    fun setNote(note: Note) {
        this.note = note
        applyStyle()
        initPaints()
    }

    fun getNote() = note!!

    // Значения по умолчанию
    private val defaultBackgroundColor = ContextCompat.getColor(context, R.color.default_background_color)
    private val defaultTextColor = ContextCompat.getColor(context, R.color.default_text_color)
    private val titleRectColor = ContextCompat.getColor(context, R.color.title_rect_color)
    private val starColor = ContextCompat.getColor(context, R.color.star_color)
    private val checkCircleColor = ContextCompat.getColor(context, R.color.check_circle_color)
    private val checkColor =  ContextCompat.getColor(context, R.color.check_color)

    // Инициализируются в init
    private var titleRectHeight = 0f
    private var starSize = 0f
    private var checkSize = 0f
    private var titleSize = 0f
    private var bodySize = 0f
    private var timeSize = 0f
    private var textHorizontalPadding = 0f
    private var textVerticalPadding = 0f
    private var defaultCornerRadius = 0f
    private var defaultElevation = 0f
    private var defaultNoteWidth = 0
    private var defaultNoteHeight = 0

    // Геометрия
    private val frameRect = RectF()
    private val shadowRect = RectF()
    private val titleFrameRect = RectF()
    private val titleFrameStroke = RectF()
    private val starRect = RectF()
    private val checkRect = RectF()

    // Стейт
    private var cornerRadius = 0f
    private var backgroundColor = defaultBackgroundColor
    private var textColor = defaultTextColor
    private var elevationSize = 0f

    // Paint
    private val backgroundPaint = Paint().apply { isAntiAlias = true }
    private val titleRectPaint = Paint().apply { isAntiAlias = true }
    private val starPaint = Paint().apply { isAntiAlias = true }
    private val titlePaint = TextPaint().apply { isAntiAlias = true }
    private val bodyPaint = TextPaint().apply { isAntiAlias = true }
    private val timePaint = TextPaint().apply { isAntiAlias = true }
    private val checkCirclePaint = Paint().apply { isAntiAlias = true }
    private val checkPaint = TextPaint().apply { isAntiAlias = true }
    private val shadowPaint = Paint().apply { isAntiAlias = true }
    private lateinit var bodyLayout: StaticLayout

    // Callback
    private var callback: OnChangeListener? = null

    init {
        initDimensions()
        initAttrs(attrs, defStyleAttr, defStyleRes)
        initPaints()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredWidth = resolveSize(defaultNoteWidth, widthMeasureSpec)
        val measuredHeight = resolveSize(defaultNoteHeight, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }
    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NoteView, defStyleAttr, defStyleRes)
            try {
                backgroundColor = typedArray.getColor(R.styleable.NoteView_noteBackgroundColor, defaultBackgroundColor)
                textColor = typedArray.getColor(R.styleable.NoteView_noteTextColor, defaultTextColor)
                cornerRadius = typedArray.getDimension(R.styleable.NoteView_noteCornerRadius, defaultCornerRadius)
                elevationSize = typedArray.getDimension(R.styleable.NoteView_noteElevation, defaultElevation)
            } finally {
                typedArray.recycle()
            }
        }
    }

    private fun applyStyle() {
        val styleRes = if (note?.isRead ?: false) R.style.NoteStyle_Read else R.style.NoteStyle_NotRead
        val typedArray = context.obtainStyledAttributes(styleRes, R.styleable.NoteView)
        try {
            if (backgroundColor == defaultBackgroundColor) {
                backgroundColor = typedArray.getColor(
                    R.styleable.NoteView_noteBackgroundColor,
                    backgroundColor
                )
            }
            if (textColor == defaultTextColor) {
                textColor = typedArray.getColor(
                    R.styleable.NoteView_noteTextColor,
                    textColor
                )
            }
        } finally {
            typedArray.recycle()
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
            textSize = titleSize
            typeface = Typeface.DEFAULT_BOLD
            color = textColor
        }
        bodyPaint.apply {
            textSize = bodySize
            typeface = Typeface.DEFAULT
            color = textColor
        }
        timePaint.apply {
            textSize = timeSize
            typeface = Typeface.DEFAULT
            color = textColor
        }
        checkCirclePaint.apply {
            style = Paint.Style.FILL
            color = checkCircleColor
        }
        checkPaint.apply {
            textSize = checkSize - 6
            typeface = Typeface.DEFAULT
            color = checkColor
        }
        shadowPaint.apply {
            style = Paint.Style.FILL
            color = Color.argb(15, 0, 0, 0)
        }
    }

    private fun initDimensions() {
        val resources = context.resources
        resources.apply {
            titleRectHeight = getDimension(R.dimen.title_rect_height)
            starSize = getDimension(R.dimen.star_size)
            checkSize = getDimension(R.dimen.check_size)
            titleSize = getDimension(R.dimen.title_size)
            bodySize = getDimension(R.dimen.body_size)
            timeSize = getDimension(R.dimen.time_size)
            textHorizontalPadding = getDimension(R.dimen.text_horizontal_padding)
            textVerticalPadding = getDimension(R.dimen.text_vertical_padding)
            defaultCornerRadius = getDimension(R.dimen.default_corner_radius)
            defaultElevation = getDimension(R.dimen.default_elevation)
            defaultNoteWidth = getDimension(R.dimen.default_note_width).toInt()
            defaultNoteHeight = getDimension(R.dimen.default_note_height).toInt()
        }
        cornerRadius = defaultCornerRadius
        elevationSize = defaultElevation
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateGeometry()
    }

    private fun updateGeometry() {
        shadowRect.set(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (width - paddingRight).toFloat(),
            (height - paddingBottom).toFloat()
        )
        frameRect.set(
            shadowRect.left,
            shadowRect.top,
            shadowRect.right - elevationSize,
            shadowRect.bottom - elevationSize
        )
        titleFrameRect.set(
            frameRect.left,
            frameRect.top,
            frameRect.right,
            paddingTop + titleRectHeight
        )
        titleFrameStroke.set(
            titleFrameRect.left,
            titleFrameRect.bottom - cornerRadius,
            titleFrameRect.right,
            titleFrameRect.bottom + cornerRadius
        )
        starRect.set(
            titleFrameRect.left + textHorizontalPadding,
            titleFrameRect.top + textVerticalPadding,
            titleFrameRect.left + textHorizontalPadding + starSize,
            titleFrameRect.top + textVerticalPadding + starSize
        )
        checkRect.set(
            frameRect.right - textHorizontalPadding - checkSize,
            frameRect.bottom - textVerticalPadding - checkSize,
            frameRect.right - textHorizontalPadding,
            frameRect.bottom - textVerticalPadding
        )

        val availableBodyWidth = (frameRect.right - frameRect.left - textHorizontalPadding * 2).toInt()
        bodyLayout = StaticLayout.Builder.obtain(
            note?.body ?: "",
            0,
            note?.body?.length ?: 0,
            bodyPaint,
            availableBodyWidth
        )
            .setMaxLines(2)
            .setEllipsize(TextUtils.TruncateAt.END)
            .setLineSpacing(1.0f, 1.2f)
            .build()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        updateGeometry()
        // Тень
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint)

        // Фон
        canvas.drawRoundRect(frameRect, cornerRadius, cornerRadius, backgroundPaint)
        canvas.drawRoundRect(titleFrameRect, cornerRadius, cornerRadius, titleRectPaint)
        canvas.drawRect(titleFrameStroke, titleRectPaint)

        // Звезда
        val starX = starRect.centerX() - starSize / 2
        val starBaseline = starRect.centerY()
        val star = if (note?.isImportant ?: false) "★" else "☆"
        canvas.drawText(star, starX, starBaseline, starPaint)

        // Текст
        val titleX = starRect.right + textHorizontalPadding
        val titleBaseline = starBaseline
        canvas.drawText(note?.title ?: "", titleX, titleBaseline, titlePaint)

        val bodyX = frameRect.left + textHorizontalPadding
        val bodyY = titleFrameStroke.bottom + textVerticalPadding
        canvas.withTranslation(bodyX, bodyY) {
            bodyLayout.draw(this)
        }

        val timeX = bodyX
        val timeBaseline = frameRect.bottom - textVerticalPadding
        val createTime = note?.getCreateTimeFormatted() ?: ""
        canvas.drawText(createTime, timeX, timeBaseline, timePaint)

        // Круг с галочкой
        if (note?.isRead ?: false) {
            canvas.drawCircle(
                checkRect.centerX(),
                checkRect.centerY(),
                checkSize / 2,
                checkCirclePaint
            )
            val checkX = checkRect.centerX() - checkSize / 3
            val checkBaseline = checkRect.bottom - checkSize / 4
            canvas.drawText("✓", checkX, checkBaseline, checkPaint)
        }
    }

    fun setOnChangeListener(callback: OnChangeListener) {
        this.callback = callback
    }

    interface OnChangeListener {
        fun onImportanceChanged(isImportant: Boolean)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (starRect.contains(touchX, touchY)) {
                    val isImportant = note?.isImportant ?: false
                    note = note?.copy(isImportant = !isImportant)
                    callback?.onImportanceChanged(!isImportant)
                    invalidate()
                    return true
                }
            }
        }

        return super.onTouchEvent(event)
    }
}