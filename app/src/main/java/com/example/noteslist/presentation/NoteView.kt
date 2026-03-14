package com.example.noteslist.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.example.noteslist.R
import androidx.core.graphics.toColorInt
import com.example.noteslist.domain.Note
import kotlin.apply
import androidx.core.graphics.withTranslation
import java.text.SimpleDateFormat

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
    private val defaultBackgroundColor = "#edf1f5".toColorInt()
    private val defaultTextColor = Color.BLACK
    private val defaultElevation = 0f
    private val titleRectColor = "#62abf0".toColorInt()
    private val starColor = "#ebc12a".toColorInt()
    private val checkCircleColor = "#40962a".toColorInt()
    private val checkColor = Color.WHITE

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

    // Геометрия
    private val frameRect = RectF()
    private val titleFrameRect = RectF()
    private val titleFrameStroke = RectF()
    private val starRect = RectF()
    private val checkRect = RectF()

    // Стейт
    private var cornerRadius = defaultCornerRadius
    private var backgroundColor = defaultBackgroundColor
    private var textColor = defaultTextColor
    private var elevation = defaultElevation

    // Paint
    private val backgroundPaint = Paint().apply { isAntiAlias = true }
    private val titleRectPaint = Paint().apply { isAntiAlias = true }
    private val starPaint = Paint().apply { isAntiAlias = true }
    private val titlePaint = TextPaint().apply { isAntiAlias = true }
    private val bodyPaint = TextPaint().apply { isAntiAlias = true }
    private val timePaint = TextPaint().apply { isAntiAlias = true }
    private val checkCirclePaint = Paint().apply { isAntiAlias = true }
    private val checkPaint = TextPaint().apply { isAntiAlias = true }
    private lateinit var bodyLayout: StaticLayout

    init {
        initDimensions()
        initAttrs(attrs, defStyleAttr, defStyleRes)
        initPaints()
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.NoteView, defStyleAttr, defStyleRes)
            try {
                backgroundColor = typedArray.getColor(R.styleable.NoteView_noteBackgroundColor, defaultBackgroundColor)
                textColor = typedArray.getColor(R.styleable.NoteView_noteTextColor, defaultTextColor)
                cornerRadius = typedArray.getDimension(R.styleable.NoteView_noteCornerRadius, defaultCornerRadius)
                elevation = typedArray.getDimension(R.styleable.NoteView_noteElevation, defaultElevation)
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
        canvas.drawCircle(checkRect.centerX(), checkRect.centerY(), checkSize / 2, checkCirclePaint)
        val checkX = checkRect.centerX() - checkSize / 3
        val checkBaseline = checkRect.bottom - checkSize / 4
        canvas.drawText("✓", checkX, checkBaseline, checkPaint)
    }
}