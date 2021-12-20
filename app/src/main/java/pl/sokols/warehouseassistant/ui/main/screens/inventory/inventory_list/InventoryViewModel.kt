package pl.sokols.warehouseassistant.ui.main.screens.inventory.inventory_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.data.repositories.InventoryRepository
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository
) : ViewModel() {

    fun getInventories(): MutableLiveData<List<Inventory>> = inventoryRepository.inventories

    fun deleteInventory(deletedInventory: Inventory) = inventoryRepository.deleteInventory(deletedInventory)

    fun addInventory(inventory: Inventory) = inventoryRepository.addInventory(inventory)
}