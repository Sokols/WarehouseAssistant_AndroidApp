package pl.sokols.warehouseassistant.data.repositories

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dagger.hilt.android.scopes.ActivityRetainedScoped
import pl.sokols.warehouseassistant.data.models.Item
import pl.sokols.warehouseassistant.services.DatabaseService
import javax.inject.Inject

@ActivityRetainedScoped
class ItemRepository @Inject constructor(
    databaseService: DatabaseService
) {

    var items: MutableLiveData<List<Item>> = MutableLiveData()

    private var itemsTable: DatabaseReference =
        databaseService.getUserTableReference().child("items")
    private var archivedItemsTable: DatabaseReference =
        databaseService.getUserTableReference().child("archived_items")

    init {
        setItemsListener()
    }

    fun getItemById(id: String): Item? = items.value?.firstOrNull { it.id == id }

    fun addItem(item: Item) {
        itemsTable.push().setValue(item)
    }

    fun updateItem(item: Item) {
        itemsTable.child(item.id).setValue(item)
        archivedItemsTable.child(item.id).removeValue()
    }

    fun deleteItem(item: Item) {
        itemsTable.child(item.id).removeValue()
        archivedItemsTable.child(item.id).setValue(item)
    }

    private fun setItemsListener() {
        itemsTable.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Item>()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(Item::class.java)?.let { item ->
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