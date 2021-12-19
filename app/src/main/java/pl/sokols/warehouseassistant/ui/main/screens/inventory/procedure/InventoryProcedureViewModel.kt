package pl.sokols.warehouseassistant.ui.main.screens.inventory.procedure

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
class InventoryProcedureViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val nfcService: NfcService
) : ViewModel() {

    private var tempItems: MutableLiveData<List<Item>> = itemRepository.items

    var items: MutableLiveData<List<Item>> = MutableLiveData(mutableListOf())

    fun retrieveNFC(intent: Intent?): Any {
        val id = nfcService.retrieveNFCMessage(intent)
        return getItemById(id) ?: NfcState.CANNOT_FIND_ITEM
    }

    fun addEditItem(item: Item, isEditing: Boolean) {
        val items = this.items.value?.toMutableList()!!

        if (isEditing) {
            items.first { it.id == item.id }.amount = item.amount
        } else {
            items.add(item)
        }

        this.items.postValue(items)
    }

    private fun getItemById(id: String): Item? = tempItems.value?.firstOrNull { it.id == id }
}