package com.spp.android.myapplication.xmlscreens.util.extensions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.spp.android.myapplication.R

class CustomGoogleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {

    private val icon = ContextCompat.getDrawable(context, R.drawable.ic_google_logo)

    private val iconSize: Int
    private val paddingBetween: Int

    init {
        background = ContextCompat.getDrawable(context, R.drawable.bg_google_button)

        text = context.getString(R.string.signup_google)
        textSize = 16f
        typeface = Typeface.DEFAULT_BOLD

        setTextColor(Color.TRANSPARENT)

        iconSize = icon?.intrinsicWidth ?: dpToPx(24)
        paddingBetween = resources.getDimensionPixelSize(R.dimen.spacer_medium)

        isAllCaps = false
    }

    override fun onDraw(canvas: Canvas) {
        val paint = paint
        paint.color = ContextCompat.getColor(context, android.R.color.black)

        val textStr = context.getString(R.string.signup_google).uppercase()
        val textWidth = paint.measureText(textStr)

        val totalWidth = iconSize + paddingBetween + textWidth
        val startX = (width - totalWidth) / 2f
        val centerY = height / 2f

        icon?.setBounds(
            startX.toInt(),
            (centerY - iconSize / 2).toInt(),
            (startX + iconSize).toInt(),
            (centerY + iconSize / 2).toInt()
        )
        icon?.draw(canvas)

        val textX = startX + iconSize + paddingBetween
        val textY = centerY - (paint.descent() + paint.ascent()) / 2
        canvas.drawText(textStr, textX, textY, paint)
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics
        ).toInt()
    }
}