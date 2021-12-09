package pl.sokols.warehouseassistant.data.models

import com.google.firebase.database.Exclude

data class Item(
    var name: String = "",
    var price: Float = 0.0f,
    var amount: Int = 0
) {

    @get:Exclude
    var id: String = ""
}
