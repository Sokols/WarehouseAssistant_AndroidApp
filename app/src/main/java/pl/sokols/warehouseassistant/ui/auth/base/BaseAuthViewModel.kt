package pl.sokols.warehouseassistant.ui.auth.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.services.AuthService
import pl.sokols.warehouseassistant.utils.AuthState
import javax.inject.Inject

@HiltViewModel
open class BaseAuthViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    open fun onInputDataChanged(email: String, password: String) {}
    open fun loginRegister(email: String, password: String) {}

    val userLiveData: MutableLiveData<FirebaseUser?> by lazy { authService.userLiveData }
    val authFormState: MutableLiveData<AuthState?> by lazy { authService.errorLiveData }
}