package pl.sokols.warehouseassistant.data.repositories

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import pl.sokols.warehouseassistant.data.models.Item
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor() {

    private var items: MutableLiveData<List<Item>> = MutableLiveData()
    private var database: DatabaseReference =
        FirebaseDatabase.getInstance("https://warehouseassistant-default-rtdb.europe-west1.firebasedatabase.app").reference
    private var itemsTable: DatabaseReference = database.child("items")

    init {
        setItemsListener()
    }

    fun getItems(): MutableLiveData<List<Item>> {
        setItemsListener()
        return items
    }

    fun addItem(item: Item) {
        itemsTable.push().setValue(item)
    }

    private fun setItemsListener() {
        itemsTable.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Item>()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(Item::class.java)?.let { item ->
                        if (item.userId == FirebaseAuth.getInstance().currentUser?.uid) {
                            item.id = dataSnapshot.key.toString()
                            list.add(item)
                        }
                    }
                }
                items.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                // Unused method
            }
        })
    }
}