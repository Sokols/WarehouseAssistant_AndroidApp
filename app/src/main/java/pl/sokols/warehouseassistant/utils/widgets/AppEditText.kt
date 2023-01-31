package pl.sokols.warehouseassistant.utils.widgets

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import pl.sokols.warehouseassistant.R
import pl.sokols.warehouseassistant.databinding.LayoutEditTextBinding

class AppEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TEXT_LIMIT_REGULAR = 40
    }

    val binding: LayoutEditTextBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.layout_edit_text,
        this,
        true
    )

    private var required = false
    private var validateRequired: String? = null
    private var validateEmail: String? = null
    private var validateMinLength: Int? = null
    private var validateMinValue: Float? = null

    init {
        context.obtainStyledAttributes(attrs, R.styleable.AppEditText).apply {
            binding.apply {
                val title = getString(R.styleable.AppEditText_title)
                required = getBoolean(R.styleable.AppEditText_required, false)
                val limit = getInteger(R.styleable.AppEditText_limit, TEXT_LIMIT_REGULAR)
                val validateTyping = getBoolean(R.styleable.AppEditText_validateTyping, false)
                validateRequired = getString(R.styleable.AppEditText_validateRequired)
                validateEmail = getString(R.styleable.AppEditText_validateEmail)
                validateMinLength = getInteger(R.styleable.AppEditText_validateMinLength, -1)
                validateMinValue = getFloat(R.styleable.AppEditText_validateMinValue, -1.0f)
                val isPassword = getBoolean(R.styleable.AppEditText_isPassword, false)

                when {
                    validateEmail != null -> etInput.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    isPassword -> etInput.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }

                tilMain.hint = title
                if (validateTyping) {
                    etInput.doAfterTextChanged { validateInternal() }
                }
                etInput.filters += InputFilter.LengthFilter(limit)
            }
            recycle()
        }
    }

    fun onTextChanged(text: (String) -> Unit) {
        binding.etInput.doAfterTextChanged { text(it.toString()) }
    }

    fun getText(): String = binding.etInput.text.toString()

    fun setText(text: String, disableValidation: Boolean = true) {
        post {
            binding.etInput.setText(text)
            if (disableValidation) showErrorState(false)
        }
    }

    private fun validateInternal() {
        validateRules(submit = false)
    }

    //region Overridden

    private fun validateRules(vararg rules: ValidationRule?, submit: Boolean = true): Boolean {
        val validationRules = mutableListOf<ValidationRule>()
        val text = getText()
        validateRequired?.let {
            // don't call this rule directly after removing the text
            if (submit || text.isNotBlank()) {
                validationRules.add(ValidationRule(text.isNotBlank(), it))
            }
        }
        validateEmail?.let {
//            validationRules.add(ValidationRule(Validation.isValidEmail(text), it))
        }

        if ((validateMinLength ?: -1) > 0 && text.isNotEmpty() && submit) {
//            validationRules.add(
//                ValidationRule(
//                    text.length >= (validateMinLength ?: -1),
//                    resources.getString(R.string.character_minimum, validateMinLength)
//                )
//            )
        }
        if ((validateMinValue ?: -1.0f) > 0 && text.isNotEmpty()) {
//            validationRules.add(
//                ValidationRule(
//                    text.toFloat() >= (validateMinValue ?: -1.0f),
//                    resources.getString(R.string.pf_minimum_required, validateMinValue?.toInt())
//                )
//            )
        }
        rules.toList().filterNotNull().let { validationRules.addAll(it) }
        return validateRules(validationRules)
    }

    private fun validateRule(rule: ValidationRule): Boolean {
        binding.apply {
            return when {
                !rule.isValid -> {
//                    tvError.text = rule.error
                    showErrorState(true)
                    false
                }
                else -> {
                    showErrorState(false)
                    true
                }
            }
        }
    }

    private fun showErrorState(show: Boolean) {
        binding.apply {
//            when (show) {
//                true -> {
//                    etInput.setBackgroundResource(R.drawable.bg_edit_text_error)
//                    if (!isCurrency) ivAlert.visibility = View.VISIBLE
//                    tvError.visibility = View.VISIBLE
//                }
//                false -> {
//                    if (isViewEnabled) {
//                        etInput.setBackgroundResource(R.drawable.bg_edit_text)
//                        if (!isCurrency) ivAlert.visibility = View.GONE
//                        cashValueView.setBackgroundResource(R.drawable.bg_green_cash_icon)
//                    }
//                    tvError.visibility = View.GONE
//                }
//            }
        }
    }

    //endregion
    fun validate(rule: ValidationRule? = null) = validateRules(rule)

    private fun validateRules(rules: List<ValidationRule>): Boolean {
        rules.forEach { rule ->
            if (!validateRule(rule)) return false
        }
        return true
    }

    //endregion
}

data class ValidationRule(val isValid: Boolean, val error: String)
