package com.tanmay.quotes.colorPicker

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import com.tanmay.quotes.R
import com.tanmay.quotes.databinding.DialogColorPickerBinding


class ColorPickerDialog(context: Context) : ViewTreeObserver.OnGlobalLayoutListener, View.OnTouchListener {

    interface OnPickColorListener {
        fun onColorPicked(color: Int)
        fun onCancel()
    }

    private var context : Context? = null
    private var _binding : DialogColorPickerBinding?=null
    private val binding get() = _binding!!
    private var selectedColor = Int.MAX_VALUE
    private var alpha = 255
    private val currentColorsHSV = floatArrayOf(1f, 1f, 1f)
    private val pickColorListener: OnPickColorListener? = null
    private val dialogTitle: String? = null
    private val dialogPositiveButtonText: String? = null
    private val dialogNegativeButtonText: String? = null
    private var dialog : Dialog?=null
    private var positiveButton: Button? = null
    private var negativeButton: Button? = null

    init {
        _binding = DialogColorPickerBinding.bind(LayoutInflater.from(context).inflate(R.layout.dialog_color_picker,null, false))
        this.context = context
    }

    @SuppressLint("ClickableViewAccessibility")
    public fun show(){
        if (selectedColor == Int.MAX_VALUE) {
            selectedColor = Color.HSVToColor(currentColorsHSV)
        }
//        if (!showAlpha) {
//            alphaImageView.setVisibility(View.GONE)
//            alphaOverlay.setVisibility(View.GONE)
//            cursorAlpha.setVisibility(View.GONE)
//            // For removing alpha if the default color passed has some alpha value.
//            // FF will make all the initial 8 bits equal to one. Doing a bitwise OR will result in
//            // the all initial 8 bits equal to 1 and thus nullify the effect of any alpha in the default color.
//            // The rest bits are zero. Doing OR with them result in the original bits.
//            selectedColor = selectedColor or -0x1000000
//        } else {
            alpha = Color.alpha(selectedColor)
//        }
        Color.colorToHSV(selectedColor, currentColorsHSV)
        binding.colorPickerView.setHue(getHue())
        val builder = AlertDialog.Builder(context)
            .setTitle(dialogTitle)
            .setView(binding.root)
            .setPositiveButton(dialogPositiveButtonText,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    pickColorListener!!.onColorPicked(
                        selectedColor
                    )
                })
            .setNegativeButton(dialogNegativeButtonText,
                DialogInterface.OnClickListener { dialogInterface, i -> pickColorListener!!.onCancel() })
            .setCancelable(true)
        dialog = builder.create()
        (dialog as AlertDialog?)?.show()

        positiveButton = (dialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE)
        negativeButton = (dialog as AlertDialog).getButton(DialogInterface.BUTTON_NEGATIVE)

        val viewTreeObserver: ViewTreeObserver = binding.root.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(this)

        binding.colorPickerView.setOnTouchListener(this)
//        hueImageView.setOnTouchListener(this)
//        alphaImageView.setOnTouchListener(this)
    }

    private fun getHue(): Float {
        return currentColorsHSV[0]
    }

    private fun isRequiredMotionEvent(motionEvent: MotionEvent): Boolean {
        return motionEvent.action == MotionEvent.ACTION_DOWN || motionEvent.action == MotionEvent.ACTION_UP || motionEvent.action == MotionEvent.ACTION_MOVE
    }

    override fun onGlobalLayout() {
        TODO("Not yet implemented")
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }
}