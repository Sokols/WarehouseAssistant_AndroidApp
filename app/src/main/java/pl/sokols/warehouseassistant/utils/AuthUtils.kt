package pl.sokols.warehouseassistant.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText

object AuthUtils {

    fun isEmailFormatValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    fun isPasswordLengthValid(password: String): Boolean {
        return password.length > 5
    }

    fun isPasswordPresentValid(password: String): Boolean {
        return password.isNotEmpty()
    }

    /**
     * Extension function to simplify setting an afterTextChanged action to EditText components.
     */
    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}