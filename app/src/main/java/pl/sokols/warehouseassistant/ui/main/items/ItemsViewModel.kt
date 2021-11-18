package pl.sokols.warehouseassistant.ui.main.items

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.data.repositories.ItemRepository
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    fun getItems() = itemRepository.getItems()
}