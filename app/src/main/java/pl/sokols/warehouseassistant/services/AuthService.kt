package pl.sokols.warehouseassistant.services

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import pl.sokols.warehouseassistant.utils.AuthState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService @Inject constructor() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val errorLiveData: MutableLiveData<AuthState?> = MutableLiveData()

    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                taskExecutor(task)
            }
    }

    fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                taskExecutor(task)
            }
    }

    fun logout() {
        firebaseAuth.signOut()
        userLiveData.postValue(null)
    }

    private fun taskExecutor(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            userLiveData.postValue(firebaseAuth.currentUser)
            errorLiveData.postValue(null)
        } else {
            when ((task.exception as FirebaseAuthException).errorCode) {
                "ERROR_USER_NOT_FOUND" -> errorLiveData.postValue(AuthState.ERROR_USER_NOT_FOUND)
                "ERROR_WRONG_PASSWORD" -> errorLiveData.postValue(AuthState.ERROR_WRONG_PASSWORD)
                "ERROR_EMAIL_ALREADY_IN_USE" -> errorLiveData.postValue(AuthState.ERROR_EMAIL_ALREADY_IN_USE)
                else -> errorLiveData.postValue(AuthState.ERROR_OTHER)
            }
        }
    }
}