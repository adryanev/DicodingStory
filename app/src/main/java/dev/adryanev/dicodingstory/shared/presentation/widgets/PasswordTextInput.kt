package dev.adryanev.dicodingstory.shared.presentation.widgets

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.utils.OnTextChangedCallback

class PasswordTextInput : TextInputEditText {

    private lateinit var onTextChangedCallback: OnTextChangedCallback

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

//    override fun onDraw(canvas: Canvas?) {
//        transformationMethod = PasswordTransformationMethod.getInstance()
//        super.onDraw(canvas)
//    }

    private fun init() {

        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        compoundDrawablePadding = 16
        hint = resources.getString(R.string.prompt_password)
        transformationMethod = PasswordTransformationMethod.getInstance()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setAutofillHints(resources.getString(R.string.prompt_password))
        }


        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty() || s.length < 6) {
                    error = resources.getString(R.string.invalid_password)
                    return
                }
                val password = s.toString()
                onTextChangedCallback(password)

            }

        })

    }

    fun setOnTextChangedCallback(callback: OnTextChangedCallback) {
        this.onTextChangedCallback = callback
    }
}