package pl.sokols.warehouseassistant.data.repositories

import pl.sokols.warehouseassistant.data.models.Item
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor() {

    // TODO: Mockup.
    fun getItems() = listOf(
        Item(id = 1, name = "Test #1", price = 1.5f),
        Item(id = 2, name = "Test #2", price = 10.5f),
        Item(id = 3, name = "Test #3", price = 100.5f),
        Item(id = 4, name = "Test #4", price = 1000.5f),
        Item(id = 5, name = "Test #5", price = 10000.5f),
        Item(id = 6, name = "Test #6", price = 1.5f),
        Item(id = 7, name = "Test #7", price = 10.5f),
        Item(id = 8, name = "Test #8", price = 100.5f),
        Item(id = 9, name = "Test #9", price = 1000.5f),
        Item(id = 10, name = "Test #10", price = 10000.5f)
    )
}