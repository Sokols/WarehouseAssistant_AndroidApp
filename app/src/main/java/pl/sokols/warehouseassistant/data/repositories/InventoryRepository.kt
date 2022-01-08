package pl.sokols.warehouseassistant.data.repositories

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.scopes.ActivityRetainedScoped
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.utils.FirebaseDatabaseUtils
import javax.inject.Inject

@ActivityRetainedScoped
class InventoryRepository @Inject constructor() {

    var inventories: MutableLiveData<List<Inventory>> = MutableLiveData()

    private var inventoriesTable: DatabaseReference =
        FirebaseDatabaseUtils.getUserTableReference().child("inventories")

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
}