package com.infinity_coder.divcalendar.presentation._common.text_watchers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class SuffixTextInputEditText : TextInputEditText {

    var suffix = ""

    private val suffixRect = Rect()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (textIsNotEmpty()) {
            val suffixPosition = paint.measureText(text.toString()) + paddingLeft
            canvas?.drawText(suffix, suffixPosition, baseline.toFloat(), paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        paint.getTextBounds(suffix, 0, suffix.length, suffixRect)
        suffixRect.right += paint.measureText(" ").toInt()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun getCompoundPaddingRight(): Int {
        return super.getCompoundPaddingRight() + suffixRect.width()
    }

    private fun textIsNotEmpty(): Boolean {
        return text != null && text.toString().isNotEmpty()
    }
}