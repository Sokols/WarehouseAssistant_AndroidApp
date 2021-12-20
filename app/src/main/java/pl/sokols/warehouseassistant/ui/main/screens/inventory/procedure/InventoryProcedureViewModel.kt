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
import pl.sokols.warehouseassistant.utils.Utils
import javax.inject.Inject

@HiltViewModel
class InventoryProcedureViewModel @Inject constructor(
    itemRepository: CountedItemRepository,
    private val inventoryRepository: InventoryRepository,
    private val nfcService: NfcService
) : ViewModel() {

    private var tempItems: MutableLiveData<List<CountedItem>> = itemRepository.items

    var items: MutableLiveData<List<CountedItem>> = MutableLiveData(mutableListOf())

    fun setItems(inventory: Inventory?) {
        inventory?.let {
            items.postValue(ArrayList(inventory.items.values).toMutableList() as List<CountedItem>)
        }
    }

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

    fun prepareInventory(existedInventory: Inventory?): Inventory? {
        val databaseItems = this.tempItems.value!!
        val items = this.items.value!!
        val tempList = mutableListOf<CountedItem>()

        items.forEach { item ->
            val tempItem = tempList.firstOrNull { item.id == it.id }
            if (tempItem == null) {
                tempList.add(item)
            } else {
                tempItem.amount += item.amount
            }
        }

        databaseItems.forEach { item ->
            val tempItem = tempList.firstOrNull { item.id == it.id }
            if (tempItem == null) {
                val databaseItem = item.copy(id = item.id)
                databaseItem.difference = databaseItem.amount * -1
                databaseItem.amount = 0
                tempList.add(databaseItem)
            } else {
                tempItem.difference = tempItem.amount - item.amount
            }
        }

        val map = tempList.associateBy({ it.id }, { it })

        return if (existedInventory != null) {
            val inventory = inventoryRepository.getInventoryById(existedInventory.timestampCreated)
            inventory?.let {
                inventory.timestampEdited = Utils.getTimestamp()
                inventory.items = map
                inventoryRepository.updateInventory(inventory)
                inventory
            }
        } else {
            val inventory = Inventory(Utils.getTimestamp(), Utils.getTimestamp(), map)
            inventoryRepository.addInventory(inventory)
            inventory
        }
    }

    fun deleteItem(index: Int) {
        val items = this.items.value?.toMutableList()!!
        items.removeAt(index)
        this.items.postValue(items)
    }

    private fun getItemById(id: String): CountedItem? = tempItems.value?.firstOrNull { it.id == id }
}