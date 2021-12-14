package pl.sokols.warehouseassistant.ui.main.inventory.procedure

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.data.repositories.InventoryRepository
import pl.sokols.warehouseassistant.data.repositories.ItemRepository
import pl.sokols.warehouseassistant.services.NfcService
import pl.sokols.warehouseassistant.utils.NfcState
import javax.inject.Inject

@HiltViewModel
class InventoryProcedureViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository,
    private val itemRepository: ItemRepository,
    private val nfcService: NfcService
) : ViewModel() {

    fun getItems(): MutableLiveData<List<Item>> = itemRepository.items

    fun retrieveNFC(intent: Intent?): Any {
        val id = nfcService.retrieveNFCMessage(intent)
        return getItemById(id) ?: NfcState.CANNOT_FIND_ITEM
    }

    private fun getItemById(id: String): Item? = itemRepository.getItemById(id)
}