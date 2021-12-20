package pl.sokols.warehouseassistant.data.repositories

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.scopes.ActivityRetainedScoped
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.services.DatabaseService
import pl.sokols.warehouseassistant.utils.Utils
import javax.inject.Inject

@ActivityRetainedScoped
class InventoryRepository @Inject constructor(
    databaseService: DatabaseService
) {

    var inventories: MutableLiveData<List<Inventory>> = MutableLiveData()

    private var inventoriesTable: DatabaseReference =
        databaseService.getUserTableReference().child("inventories")

    init {
        setInventoriesListener()
    }

    fun getInventoryById(id: String): Inventory? =
        inventories.value?.firstOrNull { it.timestampCreated == id }

    fun addInventory(inventory: Inventory) {
        inventoriesTable.child(inventory.timestampCreated).setValue(inventory)
    }

    fun updateInventory(inventory: Inventory) {
        inventoriesTable.child(inventory.timestampCreated).setValue(inventory)
    }

    fun deleteInventory(inventory: Inventory) {
        inventoriesTable.child(inventory.timestampCreated).removeValue()
    }

    private fun setInventoriesListener() {
        inventoriesTable.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Inventory>()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(Inventory::class.java)?.let { inventory ->
                        inventory.items.forEach { it.value.id = it.key }
                        list.add(inventory)
                    }
                }
                list.sortByDescending { it.timestampCreated }
                inventories.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                // Unused method
            }
        })
    }

    fun prepareInventory(items: List<CountedItem>, databaseItems: List<CountedItem>): Inventory {
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

        val map = tempList.associateBy({it.id}, {it})

        return Inventory(Utils.getTimestamp(), Utils.getTimestamp(), map)
    }
}