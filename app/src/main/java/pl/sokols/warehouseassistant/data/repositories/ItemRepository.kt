package pl.sokols.warehouseassistant.data.repositories

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import dagger.hilt.android.scopes.ActivityRetainedScoped
import pl.sokols.warehouseassistant.data.models.Item
import javax.inject.Inject
import javax.inject.Singleton

@ActivityRetainedScoped
class ItemRepository @Inject constructor() {

    private var items: MutableLiveData<List<Item>> = MutableLiveData()

    private var itemsTable: DatabaseReference = getItemsTableReference()

    init {
        setItemsListener()
    }

    fun getItems(): MutableLiveData<List<Item>> {
        setItemsListener()
        return items
    }

    fun getItemById(id: String): Item? = items.value?.firstOrNull { it.id == id }

    fun addItem(item: Item) {
        itemsTable.push().setValue(item)
    }

    fun updateItem(item: Item) {
        itemsTable.child(item.id).setValue(item)
    }

    fun deleteItem(item: Item) {
        itemsTable.child(item.id).removeValue()
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

    /**
     * Utils
     */
    private fun getItemsTableReference(): DatabaseReference = FirebaseDatabase
        .getInstance("https://warehouseassistant-default-rtdb.europe-west1.firebasedatabase.app")
        .reference
        .child(getItemsTableName())
        .child("items")

    private fun getItemsTableName(): String {
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        return firebaseUser?.uid.toString()
    }
}