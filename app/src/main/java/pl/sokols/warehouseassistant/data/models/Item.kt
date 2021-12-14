package pl.sokols.warehouseassistant.data.models

import com.google.firebase.database.Exclude

data class Item(
    var name: String = "",
    var price: Float = 1.0f,
    var amount: Int = 1
) {

    @get:Exclude
    var id: String = ""
}
