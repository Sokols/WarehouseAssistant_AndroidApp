package pl.sokols.warehouseassistant.ui.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.services.AuthService
import pl.sokols.warehouseassistant.utils.AuthState
import pl.sokols.warehouseassistant.utils.AuthUtils
import pl.sokols.warehouseassistant.utils.AuthUtils.isEmailFormatValid
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    val userLiveData: MutableLiveData<FirebaseUser?> by lazy { authService.userLiveData }
    val authFormState: MutableLiveData<AuthState?> by lazy { authService.errorLiveData }

    fun login(email: String, password: String) {
        authService.login(email, password)
    }

    fun loginDataChanged(email: String, password: String) {
        if (!isEmailFormatValid(email)) {
            authFormState.postValue(AuthState.PROVIDED_EMAIL_INVALID)
        } else if (!AuthUtils.isPasswordPresentValid(password)) {
            authFormState.postValue(AuthState.PROVIDED_PASSWORD_BLANK)
        } else {
            authFormState.postValue(AuthState.VALID)
        }
    }
}