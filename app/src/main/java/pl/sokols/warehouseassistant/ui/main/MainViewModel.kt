package pl.sokols.warehouseassistant.ui.main

import android.content.Intent
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.services.AuthService
import pl.sokols.warehouseassistant.services.NfcService
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authService: AuthService,
    private val nfcService: NfcService
) : ViewModel() {

    fun logout() {
        authService.logout()
    }

    fun retrieveNFCMessage(intent: Intent?): String = nfcService.retrieveNFCMessage(intent)
}