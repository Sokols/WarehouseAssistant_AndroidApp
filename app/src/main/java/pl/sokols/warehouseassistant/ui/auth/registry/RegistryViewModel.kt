package pl.sokols.warehouseassistant.ui.auth.registry

import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.services.AuthService
import pl.sokols.warehouseassistant.ui.auth.base.BaseAuthViewModel
import pl.sokols.warehouseassistant.utils.AuthState
import pl.sokols.warehouseassistant.utils.AuthUtils.isEmailFormatValid
import pl.sokols.warehouseassistant.utils.AuthUtils.isPasswordLengthValid
import javax.inject.Inject

@HiltViewModel
class RegistryViewModel @Inject constructor(
    private val authService: AuthService
) : BaseAuthViewModel(authService) {

    //region Overridden

    override fun loginRegister(email: String, password: String) {
        authService.register(email, password)
    }

    override fun onInputDataChanged(email: String, password: String) {
        if (!isEmailFormatValid(email)) {
            authFormState.postValue(AuthState.PROVIDED_EMAIL_INVALID)
        } else if (!isPasswordLengthValid(password)) {
            authFormState.postValue(AuthState.PROVIDED_PASSWORD_TOO_SHORT)
        } else {
            authFormState.postValue(AuthState.VALID)
        }
    }

    //endregion
}