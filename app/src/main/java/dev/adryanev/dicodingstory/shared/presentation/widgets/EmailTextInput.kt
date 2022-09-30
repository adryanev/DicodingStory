package dev.adryanev.dicodingstory.shared.presentation.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.utils.OnTextChangedCallback

class EmailTextInput : TextInputEditText {

    private lateinit var clearButtonImage: Drawable
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

    private fun init() {
        clearButtonImage =
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable
        clearButtonImage.setTint(ContextCompat.getColor(context, R.color.primaryColor))
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        hint = resources.getString(R.string.prompt_email)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    error = resources.getString(R.string.empty_email)
                    return
                }
                val email = s.toString()
                val valid = email.let { android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() }
                if (!valid) {
                    error = resources.getString(R.string.invalid_email)
                    return
                }

                onTextChangedCallback(email)

            }

        })
    }

    /**
     * Should Be called first after binding
     */
    fun setOnTextChangeCallback(onTextChangedCallback: OnTextChangedCallback) {
        this.onTextChangedCallback = onTextChangedCallback
    }

}