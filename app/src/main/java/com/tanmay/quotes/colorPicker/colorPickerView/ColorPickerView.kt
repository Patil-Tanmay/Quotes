package com.tanmay.quotes.colorPicker.colorPickerView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


/**
 * This ColorPickerView extends View class.
 * Here, I have used two Shader objects. One for horizontal gradient and one for vertical gradient.
 *
 * @author Mrudul Tora (mrudultora@gmail.com)
 * @since 6 May, 2021
 */
class ColorPickerView : View {
    var paint: Paint? = null
    var verticalShader: Shader? = null
    var horizontalShader: Shader? = null
    var hsv = floatArrayOf(1f, 1f, 1f) // hue (0-360), saturation (0-1), value (0-1)

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    /**
     * x0=0, y0=0, x1=0, y1=height (these are coordinates, assume similar to graph).
     * Color.WHITE is the start color (at (x0,y0)).
     * Color.BLACK is the end color (at (x1,y1)).
     * In between there is a vertical linear gradient.
     * (Vertical gradient is not needed to be initialized again as its value is same for every rgb)
     *
     *
     * x0=0, y0=0, x1=width, y1=0 (these are coordinates, assume similar to graph).
     * Color.WHITE is the start color (at (x0,y0)).
     * rgbValue is the end color (at (x1,y1)).
     * In between there is a horizontal linear gradient.
     *
     * @param canvas (canvas)
     */
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (paint == null) {
            paint = Paint()
        }
        val rgbValue = Color.HSVToColor(hsv)
        verticalShader = LinearGradient(
            0f, 0f, 0f, this.measuredHeight.toFloat(), Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP
        )
        horizontalShader = LinearGradient(
            0f,
            0f,
            this.measuredWidth.toFloat(),
            0f,
            Color.WHITE,
            rgbValue,
            Shader.TileMode.CLAMP
        )
        val composeShader = ComposeShader(
            verticalShader!!,
            horizontalShader!!, PorterDuff.Mode.MULTIPLY
        )
        paint!!.shader = composeShader
        canvas.drawRect(
            0f, 0f, this.measuredWidth.toFloat(), this.measuredHeight.toFloat(),
            paint!!
        )
        setLayerType(LAYER_TYPE_SOFTWARE, paint)
    }

    fun setHue(hue: Float) {
        hsv[0] = hue
        invalidate()
    }
}
