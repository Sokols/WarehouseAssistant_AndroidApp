package pl.sokols.warehouseassistant.ui.main.screens.inventory.procedure

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    var tempItems: MutableLiveData<List<Item>> = itemRepository.items

    var completedItems: MutableLiveData<List<Item>> = MutableLiveData(mutableListOf())

    var items: MutableLiveData<List<Item>> = MutableLiveData()

    fun setItems(list: List<Item>) {
        items.value = list
    }

    fun retrieveNFC(intent: Intent?): Any {
        val id = nfcService.retrieveNFCMessage(intent)
        return getItemById(items, id) ?: NfcState.CANNOT_FIND_ITEM
    }

    fun addItemToCompleted(item: Item) {
        val tempItems = items.value?.toMutableList()
        val tempCompletedItems = completedItems.value?.toMutableList()
        tempItems?.removeIf { it.id == item.id }
        tempCompletedItems?.add(item)
        completedItems.postValue(tempCompletedItems)
        items.postValue(tempItems)
    }

    private fun getItemById(list: MutableLiveData<List<Item>>, id: String): Item? =
        list.value?.firstOrNull { it.id == id }
}