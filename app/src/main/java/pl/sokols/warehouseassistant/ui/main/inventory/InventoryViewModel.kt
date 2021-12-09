package pl.sokols.warehouseassistant.ui.main.inventory

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.data.repositories.ItemRepository
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

}