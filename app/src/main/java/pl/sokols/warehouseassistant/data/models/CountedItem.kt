package pl.sokols.warehouseassistant.data.models

data class CountedItem(
    override var name: String = "",
    override var price: Float = 1.0f,
    var amount: Int = 1
) : BaseItem() {

    fun copy(id: String = this.id, name: String = this.name, price: Float = this.price, amount: Int = this.amount): CountedItem {
        val copy = CountedItem(name = name, price = price, amount = amount)
        copy.id = id
        return copy
    }
}