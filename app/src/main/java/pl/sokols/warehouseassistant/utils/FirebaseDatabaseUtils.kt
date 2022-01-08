package pl.sokols.warehouseassistant.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseDatabaseUtils {

    fun getUserTableReference(): DatabaseReference = FirebaseDatabase
        .getInstance("https://warehouseassistant-default-rtdb.europe-west1.firebasedatabase.app")
        .reference
        .child(getItemsTableName())

    private fun getItemsTableName(): String {
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        return firebaseUser?.uid.toString()
    }
}