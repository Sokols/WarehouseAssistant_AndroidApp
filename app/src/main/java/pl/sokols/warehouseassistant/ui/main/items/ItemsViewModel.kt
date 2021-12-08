package pl.sokols.warehouseassistant.ui.main.items

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.data.repositories.ItemRepository
import pl.sokols.warehouseassistant.services.NfcService
import pl.sokols.warehouseassistant.utils.NfcState
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val nfcService: NfcService
) : ViewModel() {

    private var nfcState: NfcState? = null
    private var payload: String = ""

    fun changeNfcState(nfcState: NfcState? = null, payload: String = "") {
        this.nfcState = nfcState
        this.payload = payload
    }

    fun getItems(): MutableLiveData<List<Item>> = itemRepository.getItems()

    fun addItem(item: Item) = itemRepository.addItem(item)

    fun updateItem(item: Item) = itemRepository.updateItem(item)

    fun deleteItem(deletedItem: Item) = itemRepository.deleteItem(deletedItem)

    fun retrieveNFC(intent: Intent?): String {
        return when (nfcState) {
            NfcState.WRITE -> {
                nfcService.createNFCMessage(payload, intent)
                "Written to the NFC Tag"
            }
            null -> nfcService.retrieveNFCMessage(intent)
            else -> "Error"  // do nothing
        }
    }
}