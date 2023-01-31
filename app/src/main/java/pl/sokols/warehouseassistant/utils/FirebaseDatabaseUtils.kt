package pl.sokols.warehouseassistant.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseDatabaseUtils {

    private const val FIREBASE_URL =
        "https://warehouse-assistant-88a9e-default-rtdb.europe-west1.firebasedatabase.app"

    fun getUserTableReference(): DatabaseReference = FirebaseDatabase
        .getInstance(FIREBASE_URL)
        .reference
        .child(getItemsTableName())

    private fun getItemsTableName(): String {
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        return firebaseUser?.uid.toString()
    }
}