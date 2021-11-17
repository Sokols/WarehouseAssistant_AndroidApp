package pl.sokols.warehouseassistant.ui.auth.registry

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.services.AuthService
import pl.sokols.warehouseassistant.utils.AuthState
import pl.sokols.warehouseassistant.utils.AuthUtils.isEmailFormatValid
import pl.sokols.warehouseassistant.utils.AuthUtils.isPasswordLengthValid
import javax.inject.Inject

@HiltViewModel
class RegistryViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    val userLiveData: MutableLiveData<FirebaseUser?> by lazy { authService.userLiveData }
    val authFormState: MutableLiveData<AuthState?> by lazy { authService.errorLiveData }

    fun register(email: String, password: String) {
        authService.register(email, password)
    }

    fun registerDataChanged(email: String, password: String) {
        if (!isEmailFormatValid(email)) {
            authFormState.postValue(AuthState.PROVIDED_EMAIL_INVALID)
        } else if (!isPasswordLengthValid(password)) {
            authFormState.postValue(AuthState.PROVIDED_PASSWORD_TOO_SHORT)
        } else {
            authFormState.postValue(AuthState.VALID)
        }
    }
}