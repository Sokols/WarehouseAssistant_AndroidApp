package pl.sokols.warehouseassistant.utils

import android.util.Patterns

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
}