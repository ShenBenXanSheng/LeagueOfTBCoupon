package com.example.leagueoftbcoupon.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.utils.SizeUtil

class LabelView : View {
     lateinit var mText: String
    private var mBackgroundColor: Int = 0
    private var mTextColor: Int = 0
    private var mTextSize: Int = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    @SuppressLint("Recycle")
    constructor(context: Context, attributeSet: AttributeSet?, defaultAttrs: Int) : super(
        context,
        attributeSet,
        defaultAttrs
    ) {
        initAttrs(context, attributeSet)
        initPaint()


    }

    @SuppressLint("Recycle")
    private fun initAttrs(context: Context, attributeSet: AttributeSet?) {
        val obtainStyledAttributes =
            context.obtainStyledAttributes(attributeSet, R.styleable.LabelView)
        mText = obtainStyledAttributes.getString(R.styleable.LabelView_mText).toString()

        mBackgroundColor = obtainStyledAttributes.getInt(
            R.styleable.LabelView_mBackgroundColor,
            ContextCompat.getColor(context, R.color.teal_200)
        )

        mTextColor = obtainStyledAttributes.getInt(
            R.styleable.LabelView_mTextColor,
            ContextCompat.getColor(context, R.color.black)
        )

        mTextSize = obtainStyledAttributes.getInt(
            R.styleable.LabelView_textSize,
            SizeUtil.dip2px(12f)
        )
    }

    private lateinit var backgroundPaint: Paint
    private lateinit var backgroundPath: Path
    private lateinit var textPaint: Paint
    private fun initPaint() {
        backgroundPaint = Paint()
        backgroundPaint.color = mBackgroundColor
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.isAntiAlias = true
        backgroundPaint.isDither = true

        backgroundPath = Path()

        textPaint = Paint()
        textPaint.color = mTextColor
        textPaint.style = Paint.Style.FILL
        textPaint.isAntiAlias = true
        textPaint.isDither = true
        textPaint.textSize = mTextSize.toFloat()
    }

    private var parentWidth = 0f
    private var parenHeight = 0f
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMyMeasuredDimension(
                textPaint.measureText(mText).toInt(),
                textPaint.textSize.toInt()
            )
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMyMeasuredDimension(
                textPaint.measureText(mText).toInt(),
                heightMeasureSpec
            )
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMyMeasuredDimension(
                heightMeasureSpec,
                textPaint.textSize.toInt()
            )
        } else {
            setMyMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        }


        parentWidth = measuredWidth.toFloat()
        parenHeight = measuredHeight.toFloat()
    }

    private fun setMyMeasuredDimension(width: Int, height: Int) {
        setMeasuredDimension(
            width + paddingLeft + paddingRight,
            height + paddingBottom + paddingTop
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            backgroundPath.apply {
                moveTo(0f, 0f)
                lineTo(parentWidth, 0f)
                lineTo(parentWidth, parenHeight)
                lineTo(0f, parenHeight)
                lineTo(parentWidth / 5.toFloat(), parenHeight / 2)
                lineTo(0f, 0f)
            }

            canvas.drawPath(backgroundPath, backgroundPaint)

            canvas.drawText(
                mText,
                (parentWidth * 3 / 4 - textPaint.measureText(mText) * 3 / 4)-paddingRight-paddingLeft/2,
                parenHeight / 2 + textPaint.textSize / 3,
                textPaint
            )
            // canvas.drawColor(ContextCompat.getColor(context, R.color.teal_200))
        }

    }
}