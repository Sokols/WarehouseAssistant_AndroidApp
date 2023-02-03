package pl.sokols.warehouseassistant.ui.auth.login

import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.services.AuthService
import pl.sokols.warehouseassistant.ui.auth.base.BaseAuthViewModel
import pl.sokols.warehouseassistant.utils.AuthState
import pl.sokols.warehouseassistant.utils.AuthUtils
import pl.sokols.warehouseassistant.utils.AuthUtils.isEmailFormatValid
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
) : BaseAuthViewModel(authService) {

    //region Overridden

    override fun loginRegister(email: String, password: String) {
        authService.login(email, password)
    }

    override fun onInputDataChanged(email: String, password: String) {
        if (!isEmailFormatValid(email)) {
            authFormState.postValue(AuthState.PROVIDED_EMAIL_INVALID)
        } else if (!AuthUtils.isPasswordPresentValid(password)) {
            authFormState.postValue(AuthState.PROVIDED_PASSWORD_BLANK)
        } else {
            authFormState.postValue(AuthState.VALID)
        }
    }

    //endregion
}