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

    var tempItems: MutableLiveData<List<Item>> = itemRepository.items

    var completedItems: MutableLiveData<List<Item>> = MutableLiveData(mutableListOf())

    var remainingItems: MutableLiveData<List<Item>> = MutableLiveData(mutableListOf())

    fun setItems(list: List<Item>) {
        // set items only at first coming into inventory procedure fragment
        if (remainingItems.value.isNullOrEmpty() && completedItems.value.isNullOrEmpty()) {
            remainingItems.value = list
        }
    }

    fun retrieveNFC(intent: Intent?): Any {
        val id = nfcService.retrieveNFCMessage(intent)
        return getItemById(remainingItems, id)
            ?: if (getItemById(completedItems, id) != null) NfcState.ITEM_COMPLETED
            else NfcState.CANNOT_FIND_ITEM
    }

    fun addItemToCompleted(item: Item) {
        val remainingItems = this.remainingItems.value?.toMutableList()
        val completedItems = this.completedItems.value?.toMutableList()
        val selectedRemainingItem = this.remainingItems.value?.first { it.id == item.id }!!
        val selectedCompletedItem = this.completedItems.value?.firstOrNull { it.id == item.id }

        // add item to completed items
        if (selectedCompletedItem == null) {    // if there is no item
            completedItems?.add(item)
            completedItems?.sortBy { it.name }
        } else {                                // if there is an item
            val index = completedItems?.indexOf(selectedCompletedItem)!!
            selectedCompletedItem.amount += item.amount
            completedItems[index] = selectedCompletedItem
        }

        // check that all items have been taken
        if (item.amount >= selectedRemainingItem.amount) {      // if all items have been taken
            remainingItems?.removeIf { it.id == item.id }
        } else {                                                // if not all items have been taken
            val index = remainingItems?.indexOf(selectedRemainingItem)!!
            selectedRemainingItem.amount -= item.amount
            remainingItems[index] = selectedRemainingItem
        }

        // update lists
        this.completedItems.postValue(completedItems)
        this.remainingItems.postValue(remainingItems)
    }

    fun correctCompletedItem(item: Item) {
        val remainingItems = this.remainingItems.value?.toMutableList()
        val completedItems = this.completedItems.value?.toMutableList()
        val selectedTempItem = this.tempItems.value?.first { it.id == item.id }!!
        val selectedRemainingItem = this.remainingItems.value?.firstOrNull { it.id == item.id }
        val selectedCompletedItem = this.completedItems.value?.first { it.id == item.id }!!

        val index = completedItems?.indexOf(selectedCompletedItem)!!

        if (item.amount == 0) completedItems.removeAt(index) else completedItems[index] = item

        // update remaining items only if new amount is smaller than amount from database
        if (item.amount < selectedTempItem.amount) {

            // count amount for remaining item
            val extraAmount =
                if (selectedCompletedItem.amount > selectedTempItem.amount) selectedCompletedItem.amount - selectedTempItem.amount else 0
            var amountToAdd = selectedCompletedItem.amount - item.amount - extraAmount
            amountToAdd += selectedRemainingItem?.amount ?: 0
            amountToAdd =
                if (amountToAdd > selectedTempItem.amount) selectedTempItem.amount else amountToAdd

            if (selectedRemainingItem == null) {
                selectedCompletedItem.amount = amountToAdd
                remainingItems?.add(selectedCompletedItem)
                remainingItems?.sortBy { it.name }
            } else {
                val remainingItemIndex = remainingItems?.indexOf(selectedRemainingItem)!!
                selectedRemainingItem.amount = amountToAdd
                remainingItems[remainingItemIndex] = selectedRemainingItem
            }
        }

        // update lists
        this.completedItems.postValue(completedItems)
        this.remainingItems.postValue(remainingItems)
    }

    private fun getItemById(list: MutableLiveData<List<Item>>, id: String): Item? =
        list.value?.firstOrNull { it.id == id }
}