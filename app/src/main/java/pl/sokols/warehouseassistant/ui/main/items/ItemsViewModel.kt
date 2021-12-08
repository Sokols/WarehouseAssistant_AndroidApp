package pl.sokols.warehouseassistant.ui.main.items

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.data.repositories.ItemRepository
import pl.sokols.warehouseassistant.services.NfcService
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val nfcService: NfcService
) : ViewModel() {

    fun getItems(): MutableLiveData<List<Item>> = itemRepository.getItems()

    fun addItem(item: Item) = itemRepository.addItem(item)

    fun updateItem(item: Item) = itemRepository.updateItem(item)

    fun deleteItem(deletedItem: Item) = itemRepository.deleteItem(deletedItem)

    fun retrieveNFCMessage(intent: Intent?): String = nfcService.retrieveNFCMessage(intent)

    fun createNFCMessage(payload: String, intent: Intent?): Boolean =
        nfcService.createNFCMessage(payload, intent)
}