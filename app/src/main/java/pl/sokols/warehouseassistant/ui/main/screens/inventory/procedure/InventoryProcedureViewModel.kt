package pl.sokols.warehouseassistant.ui.main.screens.inventory.procedure

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.data.repositories.CountedItemRepository
import pl.sokols.warehouseassistant.data.repositories.InventoryRepository
import pl.sokols.warehouseassistant.services.NfcService
import pl.sokols.warehouseassistant.utils.NfcState
import javax.inject.Inject

@HiltViewModel
class InventoryProcedureViewModel @Inject constructor(
    itemRepository: CountedItemRepository,
    private val inventoryRepository: InventoryRepository,
    private val nfcService: NfcService
) : ViewModel() {

    private var tempItems: MutableLiveData<List<CountedItem>> = itemRepository.items

    var items: MutableLiveData<List<CountedItem>> = MutableLiveData(mutableListOf())

    fun retrieveNFC(intent: Intent?): Any {
        val id = nfcService.retrieveNFCMessage(intent)
        return getItemById(id) ?: NfcState.CANNOT_FIND_ITEM
    }

    fun addEditItem(item: CountedItem, isEditing: Boolean, index: Int? = null) {
        val items = this.items.value?.toMutableList()!!

        if (isEditing && index != null) {
            items[index].amount = item.amount
        } else {
            items.add(item.copy(id = item.id))
        }

        this.items.postValue(items)
    }

    fun prepareInventory(): Inventory {
        val inventory =
            inventoryRepository.prepareInventory(this.items.value!!, this.tempItems.value!!)
        inventoryRepository.addInventory(inventory)
        return inventory
    }

    fun deleteItem(index: Int) {
        val items = this.items.value?.toMutableList()!!
        items.removeAt(index)
        this.items.postValue(items)
    }

    private fun getItemById(id: String): CountedItem? = tempItems.value?.firstOrNull { it.id == id }
}