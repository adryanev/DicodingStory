package dev.adryanev.dicodingstory.shared.presentation.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import dev.adryanev.dicodingstory.R

class EmailTextInput : AppCompatEditText, View.OnTouchListener {

    private lateinit var clearButtonImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        clearButtonImage =
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable

        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        hint = resources.getString(R.string.prompt_email)
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.toString()?.isNotEmpty() == true) {
                    showClearButton()
                } else {
                    hideClearButton()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrEmpty()){
                    error = resources.getString(R.string.empty_email)
                    return
                }
                val email = s?.toString()
                val valid = email?.let { android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() }
                if (valid == false) {
                    error = resources.getString(R.string.invalid_email)
                }
            }

        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }

    private fun showClearButton() {
        setButtonDrawables(endOfText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfText: Drawable? = null,
        topOfText: Drawable? = null,
        endOfText: Drawable? = null,
        bottomOfText: Drawable? = null
    ) {
        setCompoundDrawables(
            startOfText,
            topOfText,
            endOfText,
            bottomOfText
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonPressed = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    ((event?.x ?: 0.0f) < clearButtonEnd) -> isClearButtonPressed = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                    ((event?.x ?: 0.0f) > clearButtonStart) -> isClearButtonPressed = true
                }
            }

            if (isClearButtonPressed) {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage = ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_baseline_close_24
                        ) as Drawable
                        showClearButton()
                        return true

                    }
                    MotionEvent.ACTION_UP -> {
                        clearButtonImage = ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_baseline_close_24
                        ) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    else -> return false
                }
            }
        }

        return false
    }
}