package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    //Paint Object for rectangle
    private val buttonPaint = Paint().apply {
        color = context.getColor(R.color.colorPrimary)
        style = Paint.Style.FILL
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.download_text_size)
    }


    //rectangle to be drawn
    private val rect = RectF(0f, 0f, widthSize.toFloat(), heightSize.toFloat())

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }


    init {
        isClickable = true
        //setBackgroundColor(resources.getColor(R.color.colorPrimary))
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawRect(rect, buttonPaint)
            buttonPaint.color = Color.BLACK
            drawText("Download", widthSize.toFloat() / 2, heightSize.toFloat() / 2, buttonPaint)
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

}