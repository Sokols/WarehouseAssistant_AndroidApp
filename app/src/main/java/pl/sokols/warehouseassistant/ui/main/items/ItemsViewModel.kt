package pl.sokols.warehouseassistant.ui.main.items

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.data.repositories.ItemRepository
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    fun getItems(): MutableLiveData<List<Item>> = itemRepository.getItems()

    fun addItem(item: Item) = itemRepository.addItem(item)

    fun updateItem(item: Item) = itemRepository.updateItem(item)

    fun deleteItem(deletedItem: Item) = itemRepository.deleteItem(deletedItem)
}