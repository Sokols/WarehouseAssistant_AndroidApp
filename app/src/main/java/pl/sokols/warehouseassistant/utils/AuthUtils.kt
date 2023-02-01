package pl.sokols.warehouseassistant.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText

object AuthUtils {

    private const val MINIMUM_PASSWORD_LENGTH = 5
    private const val MAXIMUM_PASSWORD_LENGTH = 30

    fun isEmailFormatValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    fun isPasswordLengthValid(password: String) =
        password.length in (MINIMUM_PASSWORD_LENGTH + 1)..MAXIMUM_PASSWORD_LENGTH

    fun isPasswordPresentValid(password: String) = password.isNotEmpty()

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