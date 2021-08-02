package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat.getColor
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var downloadLabel = context.resources.getString(R.string.button_name)
    private var progress = 0f
    private val circleRadius = 30.0f
    private val circleDiameter = circleRadius * 2
    private var buttonColor = 0
    private var textColor = 0

    //rectangle to be drawn
    private val rect by lazy { RectF(0f, 0f, widthSize.toFloat(), heightSize.toFloat()) }


    //Paint Object for rectangle
    private val buttonPaint = Paint().apply {
        color = buttonColor
        style = Paint.Style.FILL
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.download_text_size)
    }

    //calculate text offset inside the button to have it centered vertically too
    private val textHeight = buttonPaint.descent() - buttonPaint.ascent()
    private val textOffset = textHeight / 2 - buttonPaint.descent()

    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f)

    //Button state observation to handle the changes and triggering the animation takes initial state as completed
    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        when (new) {
            ButtonState.Clicked -> {
                updateButtonState(ButtonState.Loading)
            }

            ButtonState.Loading -> {
                downloadLabel = context.resources.getString(R.string.button_loading)
                buttonAnimate()
            }

            ButtonState.Completed -> {
                downloadLabel = context.resources.getString(R.string.button_name)
                invalidate()
            }

        }
    }


    init {
        isClickable = true
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton).apply {
            buttonColor = getColor(R.styleable.LoadingButton_viewColor, getColor(context, R.color.colorPrimary))
            textColor = getColor(R.styleable.LoadingButton_downloadTextColor, Color.WHITE)

        }

        typedArray.recycle()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            buttonPaint.color = buttonColor
            drawRect(rect, buttonPaint)
            if (buttonState == ButtonState.Loading) {
                drawRectLoading(canvas)
                drawArcLoading(canvas)
            }
            buttonPaint.color = textColor
            drawText(downloadLabel, widthSize.toFloat() / 2, heightSize.toFloat() / 2 + textOffset, buttonPaint)

        }

    }

    //animate the rectangle drawing on top of the existing button rectangle
    private fun drawRectLoading(canvas: Canvas) {
        buttonPaint.color = getDarkColor(buttonColor)
        canvas.drawRect(0f, 0f, (widthSize * progress), heightSize.toFloat(), buttonPaint)
    }

    //animate the circle loading
    private fun drawArcLoading(canvas: Canvas) {
        buttonPaint.color = getColor(context, R.color.colorAccent)
        canvas.drawArc(0.75f * widthSize, heightSize * 0.3f, 0.75f * widthSize + circleDiameter,
                heightSize * 0.3f + circleDiameter, 0f, 360f * progress, true, buttonPaint)
    }

    private fun buttonAnimate() {
        valueAnimator.apply {
            duration = 3000
            addUpdateListener {
                progress = animatedValue as Float
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                    isEnabled = false
                }

                override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                    updateButtonState(ButtonState.Completed)
                    isEnabled = true

                }
            })
            start()

        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val minh: Int = paddingTop + paddingBottom + suggestedMinimumHeight
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
                MeasureSpec.getSize(minh),
                heightMeasureSpec,
                0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }


    fun updateButtonState(state: ButtonState) {
        buttonState = state
    }

    private fun getDarkColor(colorInput: Int): Int {
        val factor = 0.6f
        val a = Color.alpha(colorInput)
        val r = (Color.red(colorInput) * factor).roundToInt()
        val g = (Color.green(colorInput) * factor).roundToInt()
        val b = (Color.blue(colorInput) * factor).roundToInt()

        return Color.argb(a,
                min(r, 255),
                min(g, 255),
                min(b, 255))
    }

}