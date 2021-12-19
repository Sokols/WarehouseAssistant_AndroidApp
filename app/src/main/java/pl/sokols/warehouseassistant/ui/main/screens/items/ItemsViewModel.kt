package pl.sokols.warehouseassistant.ui.main.screens.items

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.data.repositories.CountedItemRepository
import pl.sokols.warehouseassistant.services.NfcService
import pl.sokols.warehouseassistant.utils.NfcState
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemRepository: CountedItemRepository,
    private val nfcService: NfcService
) : ViewModel() {

    private var nfcState: NfcState? = null
    private var payload: String = ""

    fun changeNfcState(nfcState: NfcState? = null, payload: String = "") {
        this.nfcState = nfcState
        this.payload = payload
    }

    fun getItems(): MutableLiveData<List<CountedItem>> = itemRepository.items

    fun addItem(item: CountedItem) = itemRepository.addItem(item)

    fun updateItem(item: CountedItem) = itemRepository.updateItem(item)

    fun deleteItem(deletedItem: CountedItem) = itemRepository.deleteItem(deletedItem)

    fun retrieveNFC(intent: Intent?): Any {
        return when (nfcState) {
            NfcState.WRITE -> {
                nfcService.createNFCMessage(payload, intent)
                NfcState.WRITTEN_TO_THE_TAG
            }
            null -> {
                val id = nfcService.retrieveNFCMessage(intent)
                getItemById(id) ?: NfcState.CANNOT_FIND_ITEM
            }
            else -> NfcState.ERROR  // do nothing
        }
    }

    private fun getItemById(id: String): CountedItem? = itemRepository.getItemById(id)
}