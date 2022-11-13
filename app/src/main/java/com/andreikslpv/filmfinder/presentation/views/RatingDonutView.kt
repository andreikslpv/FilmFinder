package com.andreikslpv.filmfinder.presentation.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.andreikslpv.filmfinder.R
import kotlin.math.min

private const val RATIO = 2f
private const val RADIUS_ADJUST = 0.8f
private const val DEF_VIEW_SIZE = 50
private const val RATE_MIN = 0
private const val RATE_GROUP_1_MAX = 25
private const val RATE_GROUP_2_MAX = 50
private const val RATE_GROUP_3_MAX = 75
private const val RATE_MAX = 100


class RatingDonutView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    //Овал для рисования сегментов прогресс бара
    private val oval = RectF()

    //Координаты центра View, а также Radius
    private var radius: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f

    //Толщина линии прогресса
    private var stroke = 10f

    //Значение прогресса от 0 - 100
    private var progress = 50

    private var isRated = true

    //Значения размера текста внутри кольца
    private var scaleSize = 60f

    //Краски для наших фигур
    private lateinit var strokePaint: Paint
    private lateinit var digitPaint: Paint
    private lateinit var circlePaint: Paint

    init {
        //Получаем атрибуты и устанавливаем их в соответствующие поля
        val a =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.RatingDonutView, 0, 0)
        try {
            stroke = a.getFloat(R.styleable.RatingDonutView_stroke, stroke)
            progress = a.getInt(R.styleable.RatingDonutView_progress, progress)
        } finally {
            a.recycle()
        }
        //Инициализируем первоначальные краски
        initPaint()
    }

    private fun initPaint() {
        //Краска для колец
        strokePaint = Paint().apply {
            style = Paint.Style.STROKE
            //Сюда кладем значение из поля класса, потому как у нас краски будут видоизменяться
            strokeWidth = stroke
            //Цвет мы тоже будем получать в специальном методе, потому что в зависимости от рейтинга
            //мы будем менять цвет нашего кольца
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        //Краска для цифр
        digitPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 2f
            setShadowLayer(5f, 0f, 0f, Color.DKGRAY)
            textSize = scaleSize
            typeface = Typeface.SANS_SERIF
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        //Краска для заднего фона
        circlePaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.DKGRAY
        }
    }

    private fun getPaintColor(progress: Int): Int = when (progress) {
        in RATE_MIN..RATE_GROUP_1_MAX -> Color.parseColor("#e84258")
        in RATE_GROUP_1_MAX + 1..RATE_GROUP_2_MAX -> Color.parseColor("#fd8060")
        in RATE_GROUP_2_MAX + 1..RATE_GROUP_3_MAX -> Color.parseColor("#fee191")
        in RATE_GROUP_3_MAX + 1..RATE_MAX -> Color.parseColor("#b0d8a4")
        else -> Color.GRAY
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = if (width > height) {
            height.div(RATIO)
        } else {
            width.div(RATIO)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val chosenWidth = chooseDimension(widthMode, widthSize)
        val chosenHeight = chooseDimension(heightMode, heightSize)

        val minSide = min(chosenWidth, chosenHeight)
        centerX = minSide.div(RATIO)
        centerY = minSide.div(RATIO)

        setMeasuredDimension(minSide, minSide)
    }

    private fun chooseDimension(mode: Int, size: Int) =
        when (mode) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> size
            else -> DEF_VIEW_SIZE
        }

    override fun onDraw(canvas: Canvas) {
        //Рисуем кольцо и задний фон
        drawRating(canvas)
        //Рисуем цифры
        drawText(canvas)
    }

    private fun drawRating(canvas: Canvas) {
        //Здесь мы можем регулировать размер нашего кольца
        val scale = radius * RADIUS_ADJUST
        //сохраняем канвас в текущем его состоянии, то есть все, что нарисовано до этого момента, сохраняется, в том числе и курсор
        canvas.save()
        //Перемещаем нулевые координаты канваса в центр, вы помните, так проще рисовать все круглое
        canvas.translate(centerX, centerY)
        //Устанавливаем размеры под наш овал
        oval.set(0f - scale, 0f - scale, scale, scale)
        //Рисуем задний фон(Желательно его отрисовать один раз в bitmap, так как он статичный)
        canvas.drawCircle(0f, 0f, radius, circlePaint)
        //Рисуем "арки", из них и будет состоять наше кольцо + у нас тут специальный метод
        if (isRated)
            canvas.drawArc(oval, -90f, convertProgressToDegrees(progress), false, strokePaint)
        //Восстанавливаем канвас, чтобы курсор вернулся в исходное состояние и мы могли корректно рисовать все остальное
        canvas.restore()
    }

    private fun convertProgressToDegrees(progress: Int): Float = progress * 3.6f

    private fun drawText(canvas: Canvas) {
        val message: String
        val factorY: Int
        if (isRated) {
            //Форматируем текст, чтобы мы выводили дробное число с одной цифрой после точки
            message = String.format("%.1f", progress / 10f)
            factorY = 4
        } else {
            message = "N/R"
            factorY = 6
        }
        //Получаем ширину и высоту текста, чтобы компенсировать их при отрисовке, чтобы текст был
        //точно в центре
        val widths = FloatArray(message.length)
        digitPaint.getTextWidths(message, widths)
        var advance = 0f
        for (width in widths) advance += width
        //Рисуем наш текст
        canvas.drawText(message, centerX - advance / 2, centerY + advance / factorY, digitPaint)
    }

    fun setProgress(pr: Int) {
        //Кладем новое значение в наше поле класса
        progress = pr
        isRated = progress in RATE_MIN..RATE_MAX
        //Создаем краски с новыми цветами
        initPaint()
        //вызываем перерисовку View
        invalidate()
    }
}