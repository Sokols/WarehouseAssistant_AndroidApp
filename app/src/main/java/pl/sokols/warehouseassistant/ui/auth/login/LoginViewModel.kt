package pl.sokols.warehouseassistant.ui.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.services.AuthService
import pl.sokols.warehouseassistant.utils.AuthState
import pl.sokols.warehouseassistant.utils.AuthUtils.isEmailValid
import pl.sokols.warehouseassistant.utils.AuthUtils.isPasswordValid
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    val userLiveData: MutableLiveData<FirebaseUser?> by lazy { authService.userLiveData }
    val authFormState: MutableLiveData<AuthState> = MutableLiveData()

    fun login(email: String, password: String) {
        authService.login(email, password)
    }

    fun loginDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            authFormState.postValue(AuthState.INVALID_EMAIL)
        } else if (!isPasswordValid(password)) {
            authFormState.postValue(AuthState.INVALID_PASSWORD)
        } else {
            authFormState.postValue(AuthState.VALID)
        }
    }
}