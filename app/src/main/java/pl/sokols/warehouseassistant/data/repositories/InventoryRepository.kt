package pl.sokols.warehouseassistant.data.repositories

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.scopes.ActivityRetainedScoped
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.data.models.Inventory
import pl.sokols.warehouseassistant.services.DatabaseService
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@ActivityRetainedScoped
class InventoryRepository @Inject constructor(
    databaseService: DatabaseService,
    itemsRepository: CountedItemRepository
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

        return Inventory(getTimestamp(), tempList)
    }

    @SuppressLint("NewApi")
    private fun getTimestamp(): String = DateTimeFormatter
        .ofPattern("yyyy-MM-dd_HH:mm:ss")
        .withZone(ZoneOffset.UTC)
        .format(Instant.now())
        .toString()
}