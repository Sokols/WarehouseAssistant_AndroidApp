package pl.sokols.warehouseassistant.data.models

data class Item(
    var id: Int,
    var name: String,
    var price: Float
) {
    override fun toString(): String {
        return "$id - $name"
    }
}
