package pl.sokols.warehouseassistant.data.repositories

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import dagger.hilt.android.scopes.ActivityRetainedScoped
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.services.DatabaseService
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
        inventories.value?.firstOrNull { it.timestampId == id }

    fun addInventory(inventory: Inventory) {
        inventoriesTable.push().setValue(inventory)
    }

    fun updateInventory(inventory: Inventory) {
        inventoriesTable.child(inventory.timestampId).setValue(inventory)
    }

    fun deleteInventory(inventory: Inventory) {
        inventoriesTable.child(inventory.timestampId).removeValue()
    }

    private fun setInventoriesListener() {
        inventoriesTable.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Inventory>()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(Inventory::class.java)?.let { inventory ->
                        inventory.timestampId = dataSnapshot.key.toString()
                        list.add(inventory)
                    }
                }
                // TODO: MOCKUP - remove
                list.addAll(
                    listOf(
                        Inventory(timestampId = "13-12-2021"),
                        Inventory(timestampId = "14-12-2021"),
                        Inventory(timestampId = "15-12-2021")
                    )
                )
                list.sortByDescending { it.timestampId }
                inventories.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                // Unused method
            }
        })
    }
}