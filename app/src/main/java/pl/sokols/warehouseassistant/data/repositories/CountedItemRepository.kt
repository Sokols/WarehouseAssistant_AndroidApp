package pl.sokols.warehouseassistant.data.repositories

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.scopes.ActivityRetainedScoped
import pl.sokols.warehouseassistant.data.models.CountedItem
import pl.sokols.warehouseassistant.services.DatabaseService
import javax.inject.Inject

@ActivityRetainedScoped
class CountedItemRepository @Inject constructor(
    databaseService: DatabaseService
) {

    var items: MutableLiveData<List<CountedItem>> = MutableLiveData()

    private var itemsTable: DatabaseReference =
        databaseService.getUserTableReference().child("items")
    private var archivedItemsTable: DatabaseReference =
        databaseService.getUserTableReference().child("archived_items")

    init {
        setItemsListener()
    }

    fun getItemById(id: String): CountedItem? = items.value?.firstOrNull { it.id == id }

    fun addItem(item: CountedItem): String {
        val push = itemsTable.push()
        push.setValue(item)
        return push.key!!
    }

    fun updateItem(item: CountedItem) {
        itemsTable.child(item.id).setValue(item)
        archivedItemsTable.child(item.id).removeValue()
    }

    fun deleteItem(item: CountedItem) {
        itemsTable.child(item.id).removeValue()
        archivedItemsTable.child(item.id).setValue(item)
    }

    private fun setItemsListener() {
        itemsTable.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<CountedItem>()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(CountedItem::class.java)?.let { item ->
                        item.id = dataSnapshot.key.toString()
                        list.add(item)
                    }
                }
                list.sortBy { it.name }
                items.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                // Unused method
            }
        })
    }
}