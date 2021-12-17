package pl.sokols.warehouseassistant.data.models

import com.google.firebase.database.Exclude

data class Item(
    var name: String = "",
    var price: Float = 1.0f,
    var amount: Int = 1
) {

    @get:Exclude
    var id: String = ""

    fun copy(id: String = this.id, name: String = this.name, price: Float = this.price, amount: Int = this.amount): Item {
        val copy = Item(name = name, price = price, amount = amount)
        copy.id = id
        return copy
    }
}
