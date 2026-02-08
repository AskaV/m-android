package com.spp.android.myapplication.presentation.util.extensions

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import com.squareup.picasso.Transformation

class CircleTransform : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val size = minOf(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squared = Bitmap.createBitmap(source, x, y, size, size)
        if (squared != source) {
            source.recycle()
        }

        val result = Bitmap.createBitmap(size, size, source.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint =
            Paint().apply {
                isAntiAlias = true
                shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            }

        val radius = size / 2f
        canvas.drawCircle(radius, radius, radius, paint)

        squared.recycle()
        return result
    }

    override fun key(): String = "circle"
}
