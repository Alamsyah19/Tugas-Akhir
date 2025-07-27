package com.mandiri.appta.Custom_btn


import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.mandiri.appta.R

class BtnLogin @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        background = ContextCompat.getDrawable(context, R.drawable.bg_btn_login)
        setTextColor(ContextCompat.getColor(context, android.R.color.white))
        textSize = 16f
        elevation = 6f
        isAllCaps = false
    }
}