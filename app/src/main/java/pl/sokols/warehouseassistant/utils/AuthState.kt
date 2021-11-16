package pl.sokols.warehouseassistant.utils

enum class AuthState {
    // Validation States
    VALID,
    PROVIDED_EMAIL_INVALID,
    PROVIDED_PASSWORD_TOO_SHORT,
    PROVIDED_PASSWORD_BLANK,

    // Firebase States
    ERROR_USER_NOT_FOUND,
    ERROR_WRONG_PASSWORD,
    ERROR_EMAIL_ALREADY_IN_USE,
    ERROR_OTHER
}