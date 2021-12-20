package pl.sokols.warehouseassistant.ui.main.screens.inventory.summary

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.services.EmailService
import pl.sokols.warehouseassistant.services.FileService
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val fileService: FileService,
    private val emailService: EmailService
) : ViewModel() {

    fun shareInventory(inventory: Inventory) {
        val file = fileService.generateTextFile(inventory)
        FirebaseAuth.getInstance().currentUser?.email?.let {
            emailService.sendEmail(it, subject = inventory.timestampCreated, attachment = file)
        }
    }
}