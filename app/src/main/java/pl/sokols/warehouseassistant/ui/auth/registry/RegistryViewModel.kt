package pl.sokols.warehouseassistant.ui.auth.registry

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.R
import javax.inject.Inject

@HiltViewModel
class RegistryViewModel @Inject constructor(): ViewModel() {

    fun login(username: String, password: String) {
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
        } else if (!isPasswordValid(password)) {
        } else {
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}